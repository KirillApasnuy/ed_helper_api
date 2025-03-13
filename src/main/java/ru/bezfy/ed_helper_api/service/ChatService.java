package ru.bezfy.ed_helper_api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.UserChat;
import ru.bezfy.ed_helper_api.model.UserMessage;
import ru.bezfy.ed_helper_api.model.dao.UserChatDAO;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final UserChatDAO userChatDAO;

    public ChatService(UserChatDAO userChatDAO) {
        this.userChatDAO = userChatDAO;
    }

    public UserChat createChat(LocalUser localUser, String title) {
        UserChat userChat = new UserChat();
        userChat.setTitle(title);
        userChat.setUser(localUser);
        return userChatDAO.save(userChat);
    }

    public UserChat getChat(Long chatId) {
        return userChatDAO.findById(chatId).orElse(null);
    }

    public List<UserChat> getChats(LocalUser localUser) {
        return userChatDAO.findByUserIdOrderByCreatedAtDesc(localUser.getId());
    }

    public Boolean addMessage(Long chatId, UserMessage userMessage) {
        Optional<UserChat> chat = userChatDAO.findById(chatId);
        if (chat.isPresent()) {
            chat.get().getMessages().add(userMessage);
            userChatDAO.save(chat.get());
            return true;
        }
        return false;
    }

    public ResponseEntity<String> deleteChat(Long chatId) {
        Optional<UserChat> chat = userChatDAO.findById(chatId);
        if (chat.isPresent()) {
            userChatDAO.deleteById(chatId);
            return ResponseEntity.ok("Chat deleted");
        }
        return ResponseEntity.badRequest().body("Chat not found");
    }
}
