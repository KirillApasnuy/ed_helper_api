package ru.bezfy.ed_helper_api.model.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Choice {
    public int index;
    public Message message;
    public String finish_reason;
}
