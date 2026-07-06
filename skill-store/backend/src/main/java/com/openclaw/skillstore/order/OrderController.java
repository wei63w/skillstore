package com.openclaw.skillstore.order;

import com.openclaw.skillstore.auth.AuthService;
import com.openclaw.skillstore.auth.StoreRole;
import com.openclaw.skillstore.common.ApiResponse;
import com.openclaw.skillstore.common.DemoStoreState;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final AuthService authService;
    private final DemoStoreState state;
    private final OrderService orderService;

    public OrderController(AuthService authService, DemoStoreState state, OrderService orderService) {
        this.authService = authService;
        this.state = state;
        this.orderService = orderService;
    }

    @PostMapping("/api/store/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestHeader(AuthService.TOKEN_HEADER) String token,
            @Valid @RequestBody CreateOrderRequest request) {
        DemoStoreState.UserAccount buyer = authService.requireRole(token, StoreRole.BUYER);
        return ResponseEntity.status(201).body(ApiResponse.created(orderService.createOrder(buyer, request)));
    }

    @PostMapping("/api/store/orders/{orderId}/pay")
    public ApiResponse<OrderResponse> pay(
            @RequestHeader(AuthService.TOKEN_HEADER) String token,
            @PathVariable String orderId,
            @Valid @RequestBody PayOrderRequest request) {
        DemoStoreState.UserAccount buyer = authService.requireRole(token, StoreRole.BUYER);
        return ApiResponse.ok(orderService.pay(buyer, orderId, request));
    }

    @GetMapping("/api/store/buyer/purchases")
    public ApiResponse<List<PurchaseGrantResponse>> purchases(@RequestHeader(AuthService.TOKEN_HEADER) String token) {
        DemoStoreState.UserAccount buyer = authService.requireRole(token, StoreRole.BUYER);
        return ApiResponse.ok(state.grantsForBuyer(buyer.id()).stream().map(orderService::toResponse).toList());
    }
}
