package ru.yakaska.tengen.service.impl;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.minio.MinioMapper;
import ru.yakaska.tengen.service.StorageService;
import ru.yakaska.tengen.minio.MinioObject;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("${app.minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    @Override
    public List<MinioObject> listFolder(String userFolder) {
        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(userFolder)
                        .recursive(false)
                        .build()
        );
        return MinioMapper.toMinioObject(items);
    }

    @Override
    public Map<String, MinioObject> search(String userDirectory, String userFolder) {
        return null;
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
    public boolean uploadFile(String userDirectory, MultipartFile[] file) {
        return false;
    }

    @Override
    public void renameFile(String filePath, String fileNewName) {

    }

    @Override
    public void renameDirectory(String filePath, String fileName) {

    }
}
