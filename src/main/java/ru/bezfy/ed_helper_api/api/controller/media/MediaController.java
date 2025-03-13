package ru.bezfy.ed_helper_api.api.controller.media;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.service.MediaService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("v1/media/")
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            return mediaService.downloadMedia(fileName);
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }


    @PostMapping("user/upload")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal LocalUser user, @RequestParam("file") MultipartFile file) {
        try {
            System.out.println(user.getId());
            return mediaService.uploadUserMedia(file, user);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().body("Не удалось загрузить файл: " + ex.getMessage());
        }
    }

    @GetMapping("user/{fileName:.+}")
    public ResponseEntity<?> getFileByUserIdAndName(@AuthenticationPrincipal LocalUser user, @PathVariable String fileName) {
        try {
            return mediaService.getUserMedia(user.getId(), fileName);
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().body("Ошибка при загрузке файла: " + ex.getMessage());
        }
    }

    @GetMapping("{userId}/{fileName:.+}")
    public ResponseEntity<?> getFileByUserIdAndName(@PathVariable Long userId, @PathVariable String fileName) {
        try {
            return mediaService.getUserMedia(userId, fileName);
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().body("Ошибка при загрузке файла: " + ex.getMessage());
        }
    }
}
