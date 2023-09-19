package ru.yakaska.tengen.service;

import org.springframework.web.multipart.MultipartFile;
import ru.yakaska.tengen.minio.MinioObject;

import java.util.List;
import java.util.Map;

public interface StorageService {

    List<MinioObject> listFolder(String userFolder);

    Map<String, MinioObject> search(String userDirectory, String userFolder);

    boolean createFolder(String folderName);

    boolean folderExist(String folderName);

    void deleteFolder(String[] folderName);

    boolean uploadFile(String userDirectory, MultipartFile[] file);

    void renameFile(String filePath, String fileNewName);

    void renameDirectory(String filePath, String fileName);

}
