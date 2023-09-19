package ru.yakaska.tengen.minio;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MinioObject {
    private String name;
    private Boolean isDirectory;
    private String path;
}
