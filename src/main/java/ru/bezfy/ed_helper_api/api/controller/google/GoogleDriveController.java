package ru.bezfy.ed_helper_api.api.controller.google;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.service.google.GoogleDriveUploadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("v1/drive/")
public class GoogleDriveController {
    private final GoogleDriveUploadService googleDriveUploadService;

    public GoogleDriveController(GoogleDriveUploadService googleDriveUploadService) {
        this.googleDriveUploadService = googleDriveUploadService;
    }

    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam("file")MultipartFile file) {
        try {
            // Сохраняем загруженный файл во временную директорию
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            // ID папки Google Drive, куда загружаем
            String folderId = "1k5tp0ydnWHX77v5uQEXPeuGP6dlWh64P";

            // Загружаем файл
            String fileId = googleDriveUploadService.uploadFile(tempFile, file.getContentType(), folderId);

            // Удаляем временный файл
            tempFile.delete();

            return ResponseEntity.ok("Файл загружен. ID: " + fileId);

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка загрузки файла: " + e.getMessage());
        }
    }
}
