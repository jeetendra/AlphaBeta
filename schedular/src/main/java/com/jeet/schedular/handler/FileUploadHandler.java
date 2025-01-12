package com.jeet.schedular.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

@Slf4j
@Component
public class FileUploadHandler {

    private static final String UPLOAD_DIR = "uploads";

    private void createUploadFolder(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + dirPath);
            }
        } else if (!directory.isDirectory()) {
            throw new RuntimeException("The path exists but is not a directory: " + dirPath);
        } else if (!directory.canWrite()) {
            throw new RuntimeException("Write permission denied for directory: " + dirPath);
        }
    }

    public Mono<ServerResponse> handleFileUpload(ServerRequest request) {
        return request.multipartData().flatMap(multipart -> {
            Map<String, Part> singleValueMap = multipart.toSingleValueMap();
            FilePart file = (FilePart) singleValueMap.get("file");
            try {
                File tempFile = Files.createTempFile("upload-", ".tmp").toFile();
                return file.transferTo(tempFile).then(ServerResponse.ok().bodyValue("File uploaded successfully"));
//                return file.transferTo(new File("/upload/" + file.filename())).then(ServerResponse.ok().bodyValue("File uploaded successfully"));
            } catch (IOException e) {
//                throw new RuntimeException(e);
                return ServerResponse.badRequest().bodyValue("Failed to upload");
            }

        });
    }

    public Mono<ServerResponse> handleLargeFileUpload(ServerRequest request) {
        return request.multipartData().flatMap(multipart -> {
            Map<String, Part> singleValueMap = multipart.toSingleValueMap();
            FilePart filePart = (FilePart) singleValueMap.get("file");

            try {
                // Ensure the directory exists
                createUploadFolder(UPLOAD_DIR);
            } catch (RuntimeException e) {
                return ServerResponse.status(500).bodyValue("Directory setup failed: " + e.getMessage());
            }

            Path destination = Path.of("uploads/", filePart.filename());
            System.out.println(destination.toString());
            Flux<DataBuffer> content = filePart.content();

            return DataBufferUtils.write(content, destination, StandardOpenOption.CREATE)
                    .then(ServerResponse.ok().bodyValue("File " + filePart.filename() + " uploaded successfully!"));
        }).onErrorResume(ex -> ServerResponse.status(500)
                .bodyValue("File saving failed: " + ex.getMessage()));
    }

    public Mono<ServerResponse> downloadFile(ServerRequest request) {
        String filename = request.queryParam("filename").orElse("example.txt");
        Path filePath = Path.of("uploads/", filename);

        Resource resource = new PathResource(filePath);
        return ServerResponse.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(resource);
    }
}
