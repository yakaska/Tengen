package ru.yakaska.tengen.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${app.minio.url}")
    private String url;
    @Value("${app.minio.access-key}")
    private String accessKey;
    @Value("${app.minio.secret-key}")
    private String secretKey;
    @Value("${app.minio.port}")
    private int port;
    @Bean
    public MinioClient getMinioClient(){
        return  MinioClient.builder()
                .endpoint(url, port, false)
                .credentials(accessKey, secretKey)
                .build();
    }

}
