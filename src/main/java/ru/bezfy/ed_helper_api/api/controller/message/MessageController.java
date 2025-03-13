package ru.bezfy.ed_helper_api.api.controller.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.api.model.ClientMessageBody;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.service.messager.MessageService;

import java.io.IOException;

@Controller
@RequestMapping("v1/message/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("send")
    ResponseEntity<?> sendMessage(
            @AuthenticationPrincipal LocalUser user,
            @Valid @RequestBody ClientMessageBody messageBody
    ) {
        return messageService.sendOpenAiTextMessage(user, messageBody);
    }

    @PostMapping("tts")
    public ResponseEntity<String> TTSTranscript(
            @AuthenticationPrincipal LocalUser user,
            @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
            MultipartFile audioFile
    ) {
        if (!"audio/wav".equals(contentType)) {
            System.out.println(contentType);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported media type");
        }

        if (audioFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            byte[] audioBytes = audioFile.getBytes();
            // Здесь можно добавить логику для обработки аудиофайла
            return ResponseEntity.ok(audioFile.getOriginalFilename());
        } catch (Exception e) {
            System.out.println("Failed to process audio file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process audio file");
        }

    }
}
