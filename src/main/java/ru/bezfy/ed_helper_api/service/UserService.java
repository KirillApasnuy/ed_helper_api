package ru.bezfy.ed_helper_api.service;

import com.google.api.client.util.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.UserChat;
import ru.bezfy.ed_helper_api.model.UserMessage;
import ru.bezfy.ed_helper_api.model.dao.LocalUserDAO;
import ru.bezfy.ed_helper_api.model.dao.UserChatDAO;
import ru.bezfy.ed_helper_api.model.enums.SubscribeState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final LocalUserDAO localUserDAO;
    private final UserChatDAO userChatDAO;
    private final ChatService chatService;
    private final MediaService mediaService;

    public UserService(LocalUserDAO localUserDAO, UserChatDAO userChatDAO, ChatService chatService, UserChatDAO userChatDAO1, MediaService mediaService) {
        this.localUserDAO = localUserDAO;
        this.chatService = chatService;
        this.userChatDAO = userChatDAO1;
        this.mediaService = mediaService;
    }

    public ResponseEntity<List<LocalUser>> getUsers() {
        return ResponseEntity.ok(localUserDAO.findAll());
    }

    public ResponseEntity<LocalUser> getUserById(Long id) {
        Optional<LocalUser> user = localUserDAO.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<String> updateUser(LocalUser user) {
        try {
            LocalUser updateUser = localUserDAO.findById(user.getId()).orElseThrow();
            updateUser.setEmail(user.getEmail());
            localUserDAO.save(updateUser);
            return ResponseEntity.ok("User updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user" + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteUser(Long id) {
        Optional<LocalUser> user = localUserDAO.findById(id);
        if (user.isPresent()) {
            LocalUser deletedUser = user.get();
            List<UserChat> chats = chatService.getChats(deletedUser);
            mediaService.deleteUserMedia(deletedUser.getId());
            userChatDAO.deleteAll(chats);
            localUserDAO.delete(user.get());
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.notFound().build();
    }

    public Boolean accessSendMessage(LocalUser user) {
        System.out.println(user.getSubscribeState());
        if (user.getSubscribeState().equals(SubscribeState.SUBSCRIBED)) {
            if (user.getPaidEndDate().after(new Date())) {
                System.out.println("expired date");
                return verifiedLimit(user);
            }
        } else {
            return user.getCountGenerationInLastMonth() <= 5;
        }
        return false;
    }

    Boolean verifiedLimit(LocalUser user) {
        System.out.println("checked count" + user.getCountGenerationInLastMonth() + "max" + user.getSubscription().getLimitGenerations());
        return user.getCountGenerationInLastMonth() <= user.getSubscription().getLimitGenerations();
    }
}
