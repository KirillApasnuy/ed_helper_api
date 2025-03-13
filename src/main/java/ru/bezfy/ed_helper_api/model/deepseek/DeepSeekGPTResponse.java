package ru.bezfy.ed_helper_api.model.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepSeekGPTResponse {
    public String id;
    public List<Choice> choices;
    public Usage usage;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Choice {
    public int index;
    public Message message;
    public String finish_reason;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Message {
    public String role;
    public String content;
}

