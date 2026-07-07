package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.generation.GenerationContext;
import java.util.List;
import org.junit.jupiter.api.Test;

class HttpCodeModelProviderTest {

    @Test
    void fakeProviderReturnsStructuredPatchPlan() {
        CodeModelResponse response = new HttpCodeModelProvider().generatePatch(new CodeModelRequest(
                "fake",
                "T001",
                new GenerationContext("spec", "plan", List.of("- [ ] T001"), List.of("contracts"), List.of()),
                List.of()
        ));

        assertThat(response.successful()).isTrue();
        assertThat(response.patchPlan().fileChanges()).isNotEmpty();
    }

    @Test
    void realProviderWithoutSecretIsBlockedWithoutLeakingValue() {
        CodeModelResponse response = new HttpCodeModelProvider().generatePatch(new CodeModelRequest(
                "deepseek",
                "T002",
                new GenerationContext("spec", "plan", List.of("- [ ] T002"), List.of("contracts"), List.of()),
                List.of("不要泄露密钥")
        ));

        assertThat(response.successful()).isFalse();
        assertThat(response.blockedReasons()).singleElement().asString().contains("DEEPSEEK_API_KEY");
        assertThat(response.blockedReasons()).singleElement().asString().doesNotContain("sk-");
    }
}
