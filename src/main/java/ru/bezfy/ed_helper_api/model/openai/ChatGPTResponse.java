package ru.bezfy.ed_helper_api.model.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGPTResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public List<Choice> choices;
    public Usage usage;
    @JsonProperty("service_tier")
    public String serviceTier;
    @JsonProperty("system_fingerprint")
    public String systemFingerprint;
}

