package ru.bezfy.ed_helper_api.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.bezfy.ed_helper_api.api.model.ClientMessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OpenAiRepository {
    @Value("${openai.key}")
    private String OPENAI_KEY;
    @Value("${openai.chatUrl}")
    private String OPENAI_CHAT_URL;
    @Value("${openai.assistantUrl}")
    private String ASSISTANT_URL;
    @Value("${openai.ttsUrl}")
    private String OPENAI_TTS_URL;

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> sendMessage(ClientMessageBody messageBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_KEY);

        List<Map<String, Object>> content = new ArrayList<>();
        content.add(Map.of("type", "text", "text", messageBody.getText()));

        if (messageBody.getImageInBase64() != null && !messageBody.getImageInBase64().isEmpty()) {
            content.add(Map.of("type", "image_url", "image_url", Map.of("url", messageBody.getImageInBase64())));
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", content)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(OPENAI_CHAT_URL, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> getAssistant() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_KEY);
        headers.set("OpenAI-Beta", "assistants=v2");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(ASSISTANT_URL, HttpMethod.GET, entity, String.class);
    }
}
