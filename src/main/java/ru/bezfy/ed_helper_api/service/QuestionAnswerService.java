package ru.bezfy.ed_helper_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.api.model.AssistantResponse;
import ru.bezfy.ed_helper_api.api.model.ClientMessageBody;
import ru.bezfy.ed_helper_api.api.model.FileAssistantModel;
import ru.bezfy.ed_helper_api.model.AssistantModel;
import ru.bezfy.ed_helper_api.model.dao.AssistantModelDAO;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionAnswerService {
    private final AssistantModelDAO assistantModelDAO;

    public QuestionAnswerService(AssistantModelDAO assistantModelDAO) {
        this.assistantModelDAO = assistantModelDAO;
    }

    public Optional<AssistantResponse> findBestMatch(ClientMessageBody messageBody, double threshold) {
        Optional<AssistantModel> opAssistant = assistantModelDAO.findById(Long.valueOf(messageBody.getAssistantId()));
        AssistantResponse assistantResponse = new AssistantResponse();

        if (opAssistant.isEmpty()) {
            return Optional.empty();
        }

        AssistantModel assistant = opAssistant.get();
        System.out.println(assistant.getPath());

        List<FileAssistantModel.Section> sections;
        try {
            sections = loadData(assistant.getPath());
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.toString());
            return Optional.empty();
        }

        // Ищем раздел с id = 3
        Optional<FileAssistantModel.Section> sectionWithId3 = sections.stream()
                .filter(section -> section.getId() == 3)
                .findFirst();

        if (sectionWithId3.isEmpty()) {
            System.out.println("Section with id 3 not found.");
            return Optional.empty();
        }

        FileAssistantModel.Section section = sectionWithId3.get();
        double bestRatio = 0;
        FileAssistantModel.Content bestMatchAnswer = null;

        // Перебираем подкатегории и контент только для раздела с id = 3
        for (FileAssistantModel.Subcategory subcategory : section.getSubcategories()) {
            for (FileAssistantModel.Content content : subcategory.getContent()) {
                double ratio = calculateSimilarity(messageBody.getText(), content.getQuestion());
                if (ratio > bestRatio) {
                    bestRatio = ratio;
                    bestMatchAnswer = content;
                }
            }
        }

        // Если сходство выше порога, возвращаем ответ
        if (bestRatio >= threshold) {
            assert bestMatchAnswer != null;
            assistantResponse.setMessage(bestMatchAnswer.getAnswer());
            assistantResponse.setMedia(bestMatchAnswer.getMedia());
            return Optional.of(assistantResponse);
        } else {
            return Optional.empty();
        }
    }

    private double calculateSimilarity(String userQuestion, String question) {
        // Используем Levenshtein для вычисления расстояния
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int distance = levenshteinDistance.apply(userQuestion.toLowerCase(), question.toLowerCase());
        return 1.0 - (double) distance / Math.max(userQuestion.length(), question.length());
    }

    private List<FileAssistantModel.Section> loadData(String path) throws IOException {
        // Загружаем данные из JSON файла с помощью Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        FileAssistantModel fileAssistantModel = objectMapper.readValue(new File(path), FileAssistantModel.class);
        return fileAssistantModel.getSections();
    }
}