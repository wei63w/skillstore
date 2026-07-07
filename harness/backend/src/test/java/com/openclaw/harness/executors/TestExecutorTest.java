package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestExecutorTest {

    @Test
    void mapsProfilesToControlledCommands() {
        assertThat(TestProfile.UNIT.defaultCommand()).containsExactly("mvn", "test");
        assertThat(TestProfile.BUILD.defaultCommand()).containsExactly("mvn", "verify");
        assertThat(TestProfile.ALL.defaultCommand()).containsExactly("mvn", "verify");
    }
}
