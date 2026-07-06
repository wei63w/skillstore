package com.openclaw.skillstore.flow;

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
class StoreCoreFlowSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsDuplicateUserWrongPasswordUnauthorizedUploadAndUnsafeFile() throws Exception {
        register("buyer-security", "buyer-security@example.com", "BUYER");

        mockMvc.perform(post("/api/store/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"buyer-security","email":"again@example.com","password":"Password123!","role":"BUYER"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(post("/api/store/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"buyer-security","password":"WrongPassword123!"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));

        String buyerToken = login("buyer-security");
        MockMultipartFile validFile = new MockMultipartFile("file", "safe.json", "application/json", "{}".getBytes());

        mockMvc.perform(multipart("/api/store/creator/skills")
                        .file(validFile)
                        .header("X-Store-Token", buyerToken)
                        .param("name", "Invalid Upload")
                        .param("summary", "Buyer cannot upload")
                        .param("category", "automation")
                        .param("pricingMode", "FREE")
                        .param("priceCents", "0"))
                .andExpect(status().isForbidden());

        register("creator-security", "creator-security@example.com", "CREATOR");
        String creatorToken = login("creator-security");
        MockMultipartFile unsafeFile = new MockMultipartFile("file", "unsafe.exe", "application/octet-stream", "x".getBytes());

        mockMvc.perform(multipart("/api/store/creator/skills")
                        .file(unsafeFile)
                        .header("X-Store-Token", creatorToken)
                        .param("name", "Unsafe Upload")
                        .param("summary", "Executable upload must fail")
                        .param("category", "automation")
                        .param("pricingMode", "FREE")
                        .param("priceCents", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Skill文件类型不允许"));
    }

    private void register(String username, String email, String role) throws Exception {
        mockMvc.perform(post("/api/store/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","email":"%s","password":"Password123!","role":"%s"}
                                """.formatted(username, email, role)))
                .andExpect(status().isCreated());
    }

    private String login(String username) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/store/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"Password123!"}
                                """.formatted(username)))
                .andExpect(status().isOk())
                .andReturn();

        return JsonTestSupport.read(result, "$.data.token");
    }
}
