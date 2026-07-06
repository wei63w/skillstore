package com.openclaw.skillstore.storage;

import com.openclaw.skillstore.common.DemoStoreState;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SkillPackageStorageService {

    private static final long MAX_FILE_SIZE_BYTES = 1_048_576;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".zip", ".json", ".txt");

    private final DemoStoreState state;
    private final Path uploadRoot = Path.of("runtime", "uploads", "skill-store").toAbsolutePath().normalize();

    public SkillPackageStorageService(DemoStoreState state) {
        this.state = state;
    }

    public DemoStoreState.SkillPackage save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill文件不能超过1MB");
        }

        String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String lowercaseFilename = originalFilename.toLowerCase(Locale.ROOT);
        boolean allowed = ALLOWED_EXTENSIONS.stream().anyMatch(lowercaseFilename::endsWith);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill文件类型不允许");
        }

        try {
            Files.createDirectories(uploadRoot);
            String id = state.nextId("pkg");
            String safeName = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path target = uploadRoot.resolve(id + "-" + safeName).normalize();
            if (!target.startsWith(uploadRoot)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill文件名不合法");
            }
            file.transferTo(target);
            return state.savePackage(new DemoStoreState.SkillPackage(id, originalFilename, file.getSize(), target.toString()));
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Skill文件保存失败");
        }
    }
}
