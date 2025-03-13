package ru.bezfy.ed_helper_api.model.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    public String role;
    public String content;
    public String refusal;
}
