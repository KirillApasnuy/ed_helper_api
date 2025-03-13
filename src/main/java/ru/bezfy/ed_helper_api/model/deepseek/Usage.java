package ru.bezfy.ed_helper_api.model.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
}
