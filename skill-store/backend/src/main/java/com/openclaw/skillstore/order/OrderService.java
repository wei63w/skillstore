package com.openclaw.skillstore.order;

import com.openclaw.skillstore.audit.AuditLogService;
import com.openclaw.skillstore.common.DemoStoreState;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final DemoStoreState state;
    private final AuditLogService auditLogService;

    public OrderService(DemoStoreState state, AuditLogService auditLogService) {
        this.state = state;
        this.auditLogService = auditLogService;
    }

    public OrderResponse createOrder(DemoStoreState.UserAccount buyer, CreateOrderRequest request) {
        DemoStoreState.SkillListing skill = state.findSkill(request.skillId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill不存在"));
        if (skill.auditStatus() != DemoStoreState.AuditStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill尚未通过审核");
        }
        if (state.hasGrant(buyer.id(), skill.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "已购买该Skill");
        }
        DemoStoreState.StoreOrder order = state.saveOrder(new DemoStoreState.StoreOrder(
                state.nextId("order"),
                "SO" + System.currentTimeMillis(),
                buyer.id(),
                skill.id(),
                skill.priceCents(),
                PaymentStatus.PENDING,
                Instant.now()));
        auditLogService.record(buyer.id(), "CREATE_ORDER", "ORDER", order.id(), "买家创建订单");
        return toResponse(order, null);
    }

    public OrderResponse pay(DemoStoreState.UserAccount buyer, String orderId, PayOrderRequest request) {
        DemoStoreState.StoreOrder order = state.findOrder(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在"));
        if (!order.buyerId().equals(buyer.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "不能支付他人订单");
        }
        if (order.paymentStatus() == PaymentStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "订单已支付");
        }

        PaymentStatus status = request.result() == PaymentResult.SUCCESS ? PaymentStatus.PAID : PaymentStatus.FAILED;
        DemoStoreState.StoreOrder paidOrder = new DemoStoreState.StoreOrder(
                order.id(),
                order.orderNo(),
                order.buyerId(),
                order.skillId(),
                order.amountCents(),
                status,
                order.createdAt());
        state.saveOrder(paidOrder);
        PurchaseGrantResponse grant = null;
        if (status == PaymentStatus.PAID) {
            DemoStoreState.PurchaseGrant savedGrant = state.saveGrant(new DemoStoreState.PurchaseGrant(
                    state.nextId("grant"),
                    buyer.id(),
                    order.skillId(),
                    order.id(),
                    state.nextToken()));
            grant = toResponse(savedGrant);
        }
        auditLogService.record(buyer.id(), "SIMULATE_PAYMENT", "ORDER", order.id(), "模拟支付结果: " + status);
        return toResponse(paidOrder, grant);
    }

    public PurchaseGrantResponse toResponse(DemoStoreState.PurchaseGrant grant) {
        return new PurchaseGrantResponse(grant.id(), grant.skillId(), grant.orderId(), grant.downloadToken());
    }

    private OrderResponse toResponse(DemoStoreState.StoreOrder order, PurchaseGrantResponse grant) {
        return new OrderResponse(order.id(), order.orderNo(), order.skillId(), order.amountCents(), order.paymentStatus(), grant);
    }
}
