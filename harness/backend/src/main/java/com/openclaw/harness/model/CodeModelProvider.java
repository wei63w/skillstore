package com.openclaw.harness.model;

public interface CodeModelProvider {

    String providerKey();

    CodeModelResponse generatePatch(CodeModelRequest request);
}
