package ru.bezfy.ed_helper_api.model.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {
    @JsonProperty("prompt_tokens")
    public int promptTokens;
    @JsonProperty("completion_tokens")
    public int completionTokens;
    @JsonProperty("total_tokens")
    public int totalTokens;
}
