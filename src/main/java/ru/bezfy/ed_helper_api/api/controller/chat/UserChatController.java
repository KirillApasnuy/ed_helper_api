package ru.bezfy.ed_helper_api.api.controller.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.UserChat;
import ru.bezfy.ed_helper_api.model.enums.UserType;
import ru.bezfy.ed_helper_api.service.ChatService;

import java.util.List;

@Controller
@RequestMapping("v1/chats/")
public class UserChatController {
    private final ChatService chatService;

    public UserChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("all")
    public ResponseEntity<List<UserChat>> getAllChats(@AuthenticationPrincipal LocalUser user) {
        List<UserChat> chats = chatService.getChats(user);
        if (chats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chats);
    }

    @GetMapping()
    public ResponseEntity<UserChat> getUserChat(@RequestParam Long chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteChat(@RequestParam Long chatId) {
        return chatService.deleteChat(chatId);
    }
}
