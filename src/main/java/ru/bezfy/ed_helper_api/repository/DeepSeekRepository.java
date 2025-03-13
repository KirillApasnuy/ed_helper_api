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
public class DeepSeekRepository {
    @Value("${deepseek.chatUrl}")
    private String DEEPSEEKCHATURL;
    @Value("${deepseek.key}")
    private String DEEPSEEKKEY;

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> sendMessage(ClientMessageBody messageBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(DEEPSEEKKEY);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a helpful assistant"));
        messages.add(Map.of("role", "user", "content", messageBody.getText()));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", messageBody.getModel());
        requestBody.put("messages", messages);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(DEEPSEEKCHATURL, HttpMethod.POST, entity, String.class);
    }
}
