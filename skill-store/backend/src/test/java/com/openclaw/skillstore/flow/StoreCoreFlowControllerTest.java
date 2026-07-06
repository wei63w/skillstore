package com.openclaw.skillstore.flow;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreCoreFlowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void completesRegisterUploadReviewOrderPaymentPurchaseFlow() throws Exception {
        String creatorToken = registerAndLogin("creator-flow", "creator-flow@example.com", "CREATOR");
        String adminToken = registerAndLogin("admin-flow", "admin-flow@example.com", "ADMIN");
        String buyerToken = registerAndLogin("buyer-flow", "buyer-flow@example.com", "BUYER");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "prompt-pack.json",
                MediaType.APPLICATION_JSON_VALUE,
                "{\"name\":\"PromptOps\"}".getBytes());

        MvcResult uploadResult = mockMvc.perform(multipart("/api/store/creator/skills")
                        .file(file)
                        .header("X-Store-Token", creatorToken)
                        .param("name", "PromptOps Pro")
                        .param("summary", "Production prompt workflow kit")
                        .param("category", "automation")
                        .param("pricingMode", "ONE_TIME")
                        .param("priceCents", "9900"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.auditStatus").value("PENDING_REVIEW"))
                .andReturn();

        String skillId = JsonTestSupport.read(uploadResult, "$.data.id");

        mockMvc.perform(get("/api/store/admin/reviews/pending").header("X-Store-Token", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(skillId));

        mockMvc.perform(post("/api/store/admin/reviews/{skillId}", skillId)
                        .header("X-Store-Token", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APPROVE\",\"reason\":\"演示审核通过\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.auditStatus").value("APPROVED"));

        mockMvc.perform(get("/api/store/market/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(skillId));

        MvcResult orderResult = mockMvc.perform(post("/api/store/orders")
                        .header("X-Store-Token", buyerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"skillId\":\"" + skillId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.paymentStatus").value("PENDING"))
                .andReturn();

        String orderId = JsonTestSupport.read(orderResult, "$.data.id");

        mockMvc.perform(post("/api/store/orders/{orderId}/pay", orderId)
                        .header("X-Store-Token", buyerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"result\":\"SUCCESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.paymentStatus").value("PAID"))
                .andExpect(jsonPath("$.data.grant.downloadToken").exists());

        mockMvc.perform(get("/api/store/buyer/purchases").header("X-Store-Token", buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].skillId").value(skillId))
                .andExpect(jsonPath("$.data[0].downloadToken").exists());
    }

    private String registerAndLogin(String username, String email, String role) throws Exception {
        mockMvc.perform(post("/api/store/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","email":"%s","password":"Password123!","role":"%s"}
                                """.formatted(username, email, role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.passwordHash").doesNotExist());

        MvcResult result = mockMvc.perform(post("/api/store/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"Password123!"}
                                """.formatted(username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        return JsonTestSupport.read(result, "$.data.token");
    }
}
