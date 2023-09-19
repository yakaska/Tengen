package ru.yakaska.tengen.minio;

import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MinioMapper {

    public static List<MinioObject> toMinioObject(Iterable<Result<Item>> items) {
        try {
            return StreamSupport.stream(items.spliterator(), false)
                    .map(result -> {
                        try {
                            String path = result.get().objectName();
                            if (path != null && !path.isEmpty()) {
                                String name = PathUtil.getName(path);

                                return MinioObject
                                        .builder()
                                        .name(name)
                                        .path(path)
                                        .isDirectory(result.get().isDir())
                                        .build();
                            }
                            return null;
                        } catch (RuntimeException | ErrorResponseException | InsufficientDataException |
                                 InternalException | InvalidKeyException | InvalidResponseException | IOException |
                                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
                            throw new RuntimeException("Error converting Minio objects", e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            throw new RuntimeException("Error converting Minio objects", e);
        }

    }


}
