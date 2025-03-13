package ru.bezfy.ed_helper_api.api.model;

public class ClientMessageBody {
    private String text;
    private String imageUrl;
    private String audioUrl;
    private Boolean isUser;
    private Boolean isVoiceMessage;
    private String model;
    private String imageInBase64;
    private String assistantId;
    private Long chatId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImageInBase64() {
        return imageInBase64;
    }

    public void setImageInBase64(String imageInBase64) {
        this.imageInBase64 = imageInBase64;
    }

    @Override
    public String toString() {
        return "ClientMessageBody{" + "text='" + text + '\'' + ", model='" + model + '\'' + '}';
    }

    public String getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(String assistantId) {
        this.assistantId = assistantId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public Boolean getVoiceMessage() {
        return isVoiceMessage;
    }

    public void setVoiceMessage(Boolean voiceMessage) {
        isVoiceMessage = voiceMessage;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
