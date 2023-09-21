package ru.yakaska.tengen.service.impl;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.exception.FileUploadException;
import ru.yakaska.tengen.minio.MinioMapper;
import ru.yakaska.tengen.minio.MinioObject;
import ru.yakaska.tengen.service.StorageService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// format: user-{id}/

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("${app.minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    @Override
    public List<MinioObject> listDirectory(String userDirectory) {
        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(userDirectory)
                        .recursive(false)
                        .build()
        );
        return MinioMapper.toMinioObject(items);
    }

    // TODO major refactor needed
    @Override
    public Map<String, MinioObject> search(String userDirectory, String query) {
        List<MinioObject> objects = MinioMapper.toMinioObject(minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(userDirectory)
                        .recursive(true)
                        .build()
        ));

        Map<String, MinioObject> searchResults = new HashMap<>();

        for (MinioObject object : objects) {
            if (object.getName().contains(query)) {
                searchResults.put(object.getPath(), object);
            }
        }
        return searchResults;
    }

    @Override
    public boolean createFolder(String folderName) {
        return false;
    }

    @Override
    public boolean folderExist(String folderName) {
        return false;
    }

    @Override
    public void deleteFolder(String[] folderName) {
    }

    @Override
    public boolean uploadFiles(String userDirectory, MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                InputStream in = new ByteArrayInputStream(file.getBytes());
                String fileName = file.getOriginalFilename();
                String objectName = fileName; // TODO: replace with actual user folder

                minioClient.putObject(
                        PutObjectArgs
                                .builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(in, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            return true;
        } catch (Exception e) { // TODO: replace with custom exception
            throw new FileUploadException("Failed upload file");
        }
    }

    @Override
    public void renameFile(String filePath, String fileNewName) {

    }

    @Override
    public void renameDirectory(String filePath, String fileName) {

    }
}
