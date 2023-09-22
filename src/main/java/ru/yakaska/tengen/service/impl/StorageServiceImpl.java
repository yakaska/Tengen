package ru.yakaska.tengen.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.exception.FileUploadException;
import ru.yakaska.tengen.minio.MinioMapper;
import ru.yakaska.tengen.minio.MinioObject;
import ru.yakaska.tengen.service.StorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
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
    public boolean createDirectory(String name) {
        if (directoryExists(name))
            return false;

        // This path allows to create an empty folder in minio
        String objectName = name + "/";

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new FileUploadException("Failed to create directory with name:" + name);
        }
        return true;
    }

    @Override
    public boolean directoryExists(String name) {
        return listDirectory(name).iterator().hasNext();
    }

    @Override
    public void deleteFolder(String directory) {
        List<DeleteObject> deleteObjects = MinioMapper.toDeleteObject(minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(directory)
                .recursive(true)
                .build()
        ));

        Iterable<Result<DeleteError>> deleteErrors = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucket)
                        .objects(deleteObjects)
                        .build()
        );

        deleteErrors.forEach(result -> {
            DeleteError error;
            try {
                error = result.get();
            } catch (Exception e) {
                throw new RuntimeException(e); // TODO: replace with custom exception
            }
            log.warn("Error deleting file: " + error.objectName() + "; " + error.message());
        });
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
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload a file");
        }
    }

    @Override
    public void renameFile(String filePath, String fileNewName) {

    }

    @Override
    public void renameDirectory(String filePath, String fileName) {

    }
}
