package ru.yakaska.tengen.controller.storage;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.entity.User;
import ru.yakaska.tengen.minio.MinioObject;
import ru.yakaska.tengen.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    @GetMapping
    public List<MinioObject> listFolder(@RequestParam String path) {
        return storageService.listDirectory(path);
    }

    @PostMapping("upload")
    public boolean uploadFiles( // TODO: change return entity
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile[] files
    ) {
        return storageService.uploadFiles("", files);
    }

}
