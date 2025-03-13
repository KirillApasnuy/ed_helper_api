package ru.bezfy.ed_helper_api.api.controller.assistent;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.enums.UserType;
import ru.bezfy.ed_helper_api.service.AssistantService;

import java.io.IOException;

@RestController
@RequestMapping("v1/assistants/")
public class AssistantController {
    private final AssistantService assistantService;


    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping()
    public ResponseEntity<String> getAssistants() {
        try {
            return assistantService.getAssistants();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("add")
    public ResponseEntity<String> addAssistant(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null || filename.isEmpty()) {
                return ResponseEntity.badRequest().body("Filename is missing!");
            }

            String content = new String(file.getBytes()); // Читаем JSON как строку
            assistantService.saveAssistant(filename, content);

            return ResponseEntity.ok("File " + filename + " uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAssistant(@AuthenticationPrincipal LocalUser user, @RequestParam Long id) {
        if (user.getUserType().equals(UserType.TEACHER) || user.getUserType().equals(UserType.ADMIN)) {

            return assistantService.deleteAssistant(id);
        }
        return ResponseEntity.badRequest().body("You are not authorized to delete this assistant");
    }

}
