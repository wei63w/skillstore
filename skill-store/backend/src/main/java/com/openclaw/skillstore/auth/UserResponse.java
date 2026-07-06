package com.openclaw.skillstore.auth;

public record UserResponse(String id, String username, String email, StoreRole role) {
}
