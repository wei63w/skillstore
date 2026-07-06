package com.openclaw.skillstore.common;

import com.openclaw.skillstore.auth.StoreRole;
import com.openclaw.skillstore.order.PaymentStatus;
import com.openclaw.skillstore.skill.PricingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class DemoStoreState {

    private final AtomicLong sequence = new AtomicLong(1000);
    private final Map<String, UserAccount> users = new LinkedHashMap<>();
    private final Map<String, AuthSession> sessions = new LinkedHashMap<>();
    private final Map<String, SkillPackage> packages = new LinkedHashMap<>();
    private final Map<String, SkillListing> skills = new LinkedHashMap<>();
    private final Map<String, StoreOrder> orders = new LinkedHashMap<>();
    private final Map<String, PurchaseGrant> grants = new LinkedHashMap<>();
    private final List<AuditEvent> auditEvents = new ArrayList<>();

    public synchronized String nextId(String prefix) {
        return prefix + "-" + sequence.incrementAndGet();
    }

    public synchronized String nextToken() {
        return "demo-" + UUID.randomUUID();
    }

    public synchronized boolean usernameExists(String username) {
        return users.values().stream().anyMatch(user -> user.username().equalsIgnoreCase(username));
    }

    public synchronized UserAccount saveUser(UserAccount user) {
        users.put(user.id(), user);
        return user;
    }

    public synchronized Optional<UserAccount> findUserByUsername(String username) {
        return users.values().stream().filter(user -> user.username().equalsIgnoreCase(username)).findFirst();
    }

    public synchronized Optional<UserAccount> findUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public synchronized AuthSession saveSession(AuthSession session) {
        sessions.put(session.token(), session);
        return session;
    }

    public synchronized Optional<AuthSession> findSession(String token) {
        return Optional.ofNullable(sessions.get(token));
    }

    public synchronized SkillPackage savePackage(SkillPackage skillPackage) {
        packages.put(skillPackage.id(), skillPackage);
        return skillPackage;
    }

    public synchronized SkillListing saveSkill(SkillListing skill) {
        skills.put(skill.id(), skill);
        return skill;
    }

    public synchronized Optional<SkillListing> findSkill(String id) {
        return Optional.ofNullable(skills.get(id));
    }

    public synchronized List<SkillListing> pendingSkills() {
        return skills.values().stream().filter(skill -> skill.auditStatus() == AuditStatus.PENDING_REVIEW).toList();
    }

    public synchronized List<SkillListing> approvedSkills() {
        return skills.values().stream().filter(skill -> skill.auditStatus() == AuditStatus.APPROVED).toList();
    }

    public synchronized StoreOrder saveOrder(StoreOrder order) {
        orders.put(order.id(), order);
        return order;
    }

    public synchronized Optional<StoreOrder> findOrder(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public synchronized boolean hasGrant(String buyerId, String skillId) {
        return grants.values().stream()
                .anyMatch(grant -> grant.buyerId().equals(buyerId) && grant.skillId().equals(skillId));
    }

    public synchronized PurchaseGrant saveGrant(PurchaseGrant grant) {
        grants.put(grant.id(), grant);
        return grant;
    }

    public synchronized List<PurchaseGrant> grantsForBuyer(String buyerId) {
        return grants.values().stream().filter(grant -> grant.buyerId().equals(buyerId)).toList();
    }

    public synchronized void appendAudit(AuditEvent event) {
        auditEvents.add(event);
    }

    public synchronized List<AuditEvent> auditEvents() {
        return List.copyOf(auditEvents);
    }

    public record UserAccount(
            String id,
            String username,
            String email,
            String passwordHash,
            StoreRole role,
            String status,
            Instant createdAt) {
    }

    public record AuthSession(String token, String userId, Instant createdAt) {
    }

    public record SkillPackage(String id, String originalFilename, long sizeBytes, String storagePath) {
    }

    public record SkillListing(
            String id,
            String creatorId,
            String name,
            String summary,
            String category,
            PricingMode pricingMode,
            int priceCents,
            String packageId,
            AuditStatus auditStatus,
            String reviewReason,
            Instant createdAt) {
    }

    public record StoreOrder(
            String id,
            String orderNo,
            String buyerId,
            String skillId,
            int amountCents,
            PaymentStatus paymentStatus,
            Instant createdAt) {
    }

    public record PurchaseGrant(String id, String buyerId, String skillId, String orderId, String downloadToken) {
    }

    public record AuditEvent(
            String id,
            String actorId,
            String action,
            String targetType,
            String targetId,
            String message,
            Instant createdAt) {
    }

    public enum AuditStatus {
        PENDING_REVIEW,
        APPROVED,
        REJECTED
    }
}
