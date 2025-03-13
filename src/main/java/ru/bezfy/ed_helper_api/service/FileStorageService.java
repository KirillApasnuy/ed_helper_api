package ru.bezfy.ed_helper_api.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {
    private static final String STORAGE_DIR = "assistants";

    public void saveJsonFile(String filename, String jsonContent) throws IOException {
        Path dirPath = Path.of(STORAGE_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        try (FileOutputStream fos = new FileOutputStream(dirPath.resolve(filename).toFile())) {
            fos.write(jsonContent.getBytes(StandardCharsets.UTF_8));
        }
    }
}

