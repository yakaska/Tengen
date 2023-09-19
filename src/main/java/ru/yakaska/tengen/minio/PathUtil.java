package ru.yakaska.tengen.minio;

import java.nio.file.Paths;

public class PathUtil {

    public static String getName(String path) {
        return Paths.get(path).getFileName().toString();
    }

}
