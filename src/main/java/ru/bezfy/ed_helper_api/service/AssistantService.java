package ru.bezfy.ed_helper_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.AssistantModel;
import ru.bezfy.ed_helper_api.model.dao.AssistantModelDAO;
import ru.bezfy.ed_helper_api.repository.OpenAiRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class AssistantService {

    private static final String STORAGE_ASSISTANT_DIR = "assistants";

    private final AssistantModelDAO assistantModelDAO;
    private ObjectMapper objectMapper = new ObjectMapper();


    private final OpenAiRepository openAiRepositoryRepository;

    public AssistantService(AssistantModelDAO assistantModelDAO, OpenAiRepository openAiRepositoryRepository) {
        this.assistantModelDAO = assistantModelDAO;
        this.openAiRepositoryRepository = openAiRepositoryRepository;
    }

    public ResponseEntity<String> getAssistants() throws JsonProcessingException {
        Optional<List<AssistantModel>> assistantModelOptional = Optional.of(assistantModelDAO.findAll());
        return ResponseEntity.ok(objectMapper.writeValueAsString(assistantModelOptional.get()));
    }

    public ResponseEntity<String> saveAssistant(String filename, String jsonContent) throws IOException {
        Path dirPath = Path.of(STORAGE_ASSISTANT_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        try (FileOutputStream fos = new FileOutputStream(dirPath.resolve(filename).toFile())) {
            fos.write(jsonContent.getBytes(StandardCharsets.UTF_8));
            AssistantModel assistantModel = new AssistantModel();
            assistantModel.setName(filename.split("\\.")[0]);
            assistantModel.setPath(dirPath.resolve(filename).toString());
            assistantModelDAO.save(assistantModel);
            return ResponseEntity.ok(objectMapper.writeValueAsString(assistantModel));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteAssistant(Long assistantId) {
        Optional<AssistantModel> opAssistant = assistantModelDAO.findById(assistantId);
        if (opAssistant.isPresent()) {
            AssistantModel assistantModel = opAssistant.get();
            boolean isDeleted = new File(assistantModel.getPath()).delete();
            if (isDeleted) {
                assistantModelDAO.deleteById(assistantId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("Assistant not be deleted");
        }
        return ResponseEntity.badRequest().body("Assistant not found");
    }
}
