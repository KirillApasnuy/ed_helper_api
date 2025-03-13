package ru.bezfy.ed_helper_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_message")
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", length = 4096)
    private String text;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "audio_url")
    private String audioUrl;
    @Column(name = "is_user")
    private Boolean isUser = false;
    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();
    @Column(name = "media")
    @ElementCollection
    private List<String> media;
    @ManyToOne
    @JoinColumn(name = "user_chat_id")
    @JsonIgnore
    private UserChat userChat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Boolean isUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UserChat getUserChat() {
        return userChat;
    }

    public void setUserChat(UserChat userChat) {
        this.userChat = userChat;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }
}
