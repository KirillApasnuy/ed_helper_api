package ru.bezfy.ed_helper_api.api.model;

import java.util.List;

public class AssistantResponse {
    private Long chatId;
    private String message;
    private List<String> media;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }
}
