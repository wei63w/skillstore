package com.openclaw.skillstore.auth;

public record AuthResponse(String token, UserResponse user) {
}
