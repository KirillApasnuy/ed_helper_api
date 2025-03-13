package ru.bezfy.ed_helper_api.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.model.LocalUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MediaService {
    private final Path fileAssistantStorageLocation = Paths.get("assistants/3ds_max_complete/media").toAbsolutePath().normalize();
    private final Path fileStorageLocation = Paths.get("media").toAbsolutePath().normalize();

    public ResponseEntity<?> downloadMedia(String fileName) throws MalformedURLException {
        // Загружаем файл по имени
        Path filePath = this.fileAssistantStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // Проверяем, существует ли файл
        if (resource.exists() || resource.isReadable()) {
            // Определяем тип контента файла на основе его расширения
            String contentType = getContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> uploadUserMedia(MultipartFile file, LocalUser user) throws IOException {
        // Создаем папку для пользователя, если она не существует
        Path userDir = this.fileStorageLocation.resolve("client/user/" + user.getId()).normalize();
        try {
            Files.createDirectories(userDir);
            System.out.println(userDir.toString());
        } catch (IOException e) {
            e.printStackTrace(); // Логируем ошибку
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось создать папку для пользователя: " + e.getMessage());
        }

        // Сохраняем файл в папку пользователя
        Path targetLocation = userDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok().body("Файл успешно загружен: " + file.getOriginalFilename());
    }
    public ResponseEntity<?> getUserMedia(Long userId, String fileName) throws MalformedURLException {
        // Определяем путь к файлу в папке пользователя
        Path userDir = this.fileStorageLocation.resolve("client/user/" + userId).normalize();
        Path filePath = userDir.resolve(fileName).normalize();

        // Загружаем файл
        Resource resource = new UrlResource(filePath.toUri());

        // Проверяем, существует ли файл
        if (resource.exists() || resource.isReadable()) {
            // Определяем тип контента файла на основе его расширения
            String contentType = getContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Boolean deleteUserMedia(Long userId) {
        try {
            // Получаем путь к папке пользователя
            Path userDir = this.fileStorageLocation.resolve("client/user/" + userId).normalize();

            // Удаляем папку пользователя вместе со всеми файлами внутри
            Files.deleteIfExists(userDir);

            return true;
        } catch (IOException e) {
            e.printStackTrace(); // Логируем ошибку
            return false;
        }
    }
    // Метод для определения MIME-типа файла по его расширению
    private String getContentType(String fileName) {
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            // По умолчанию возвращаем общий тип для бинарных данных
            return "application/octet-stream";
        }
    }
}

