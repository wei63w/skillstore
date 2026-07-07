package com.openclaw.harness.retry;

import com.openclaw.harness.gates.HumanGate;
import java.util.List;

public record RetryOutcome(List<RetryAttempt> attempts, HumanGate humanGate) {
}
