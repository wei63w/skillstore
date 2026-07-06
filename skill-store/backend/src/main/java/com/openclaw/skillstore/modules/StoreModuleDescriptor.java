package com.openclaw.skillstore.modules;

import java.util.List;

public record StoreModuleDescriptor(
        String moduleKey,
        String displayName,
        String responsibility,
        List<String> capabilities
) {
}
