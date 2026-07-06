package com.openclaw.skillstore.api;

import com.openclaw.skillstore.common.ApiResponse;
import com.openclaw.skillstore.modules.StoreModuleCatalog;
import com.openclaw.skillstore.modules.StoreModuleDescriptor;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreModuleController {

    private final StoreModuleCatalog catalog;

    public StoreModuleController(StoreModuleCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/api/store/modules")
    public ApiResponse<List<StoreModuleDescriptor>> listModules() {
        return ApiResponse.ok(catalog.listModules());
    }
}
