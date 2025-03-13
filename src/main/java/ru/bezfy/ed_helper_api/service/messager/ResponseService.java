package ru.bezfy.ed_helper_api.service.messager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.deepseek.DeepSeekGPTResponse;
import ru.bezfy.ed_helper_api.model.openai.ChatGPTResponse;

@Service
public class ResponseService {
    ObjectMapper objectMapper = new ObjectMapper();

    public ChatGPTResponse parseOpenAiResponse(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, ChatGPTResponse.class);
    }

    public DeepSeekGPTResponse parseDeepSeekResponse(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, DeepSeekGPTResponse.class);
    }
}
