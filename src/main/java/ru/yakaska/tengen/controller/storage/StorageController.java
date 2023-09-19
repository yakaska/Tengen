package ru.yakaska.tengen.controller.storage;


import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.minio.MinioObject;
import ru.yakaska.tengen.service.StorageService;

import java.nio.file.Paths;
import java.util.List;

import static java.text.Normalizer.normalize;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    @GetMapping
    public List<MinioObject> listFolder(@RequestParam String path) {
        return storageService.listFolder(path);
    }

    @PostMapping("upload")
    public MinioObject upload(@RequestParam("file") MultipartFile file) {


        Paths.get(file.getOriginalFilename()).normalize().toAbsolutePath().toString();
        

    }

}
