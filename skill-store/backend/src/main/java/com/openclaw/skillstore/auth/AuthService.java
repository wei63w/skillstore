package com.openclaw.skillstore.auth;

import com.openclaw.skillstore.audit.AuditLogService;
import com.openclaw.skillstore.common.DemoStoreState;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    public static final String TOKEN_HEADER = "X-Store-Token";

    private final DemoStoreState state;
    private final AuditLogService auditLogService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(DemoStoreState state, AuditLogService auditLogService) {
        this.state = state;
        this.auditLogService = auditLogService;
    }

    public UserResponse register(RegisterRequest request) {
        if (state.usernameExists(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "用户名已存在");
        }

        DemoStoreState.UserAccount user = new DemoStoreState.UserAccount(
                state.nextId("user"),
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role(),
                "ACTIVE",
                Instant.now());
        state.saveUser(user);
        auditLogService.record(user.id(), "REGISTER", "USER", user.id(), "用户注册: " + user.role());
        return toResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        DemoStoreState.UserAccount user = state.findUserByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));
        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        DemoStoreState.AuthSession session = state.saveSession(new DemoStoreState.AuthSession(
                state.nextToken(),
                user.id(),
                Instant.now()));
        auditLogService.record(user.id(), "LOGIN", "USER", user.id(), "用户登录");
        return new AuthResponse(session.token(), toResponse(user));
    }

    public DemoStoreState.UserAccount requireUser(String token) {
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "缺少登录令牌");
        }
        DemoStoreState.AuthSession session = state.findSession(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录令牌无效"));
        return state.findUserById(session.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录用户不存在"));
    }

    public DemoStoreState.UserAccount requireRole(String token, StoreRole role) {
        DemoStoreState.UserAccount user = requireUser(token);
        if (user.role() != role) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "当前角色无权执行该操作");
        }
        return user;
    }

    public UserResponse toResponse(DemoStoreState.UserAccount user) {
        return new UserResponse(user.id(), user.username(), user.email(), user.role());
    }
}
