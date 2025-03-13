package ru.bezfy.ed_helper_api.api.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.enums.UserType;
import ru.bezfy.ed_helper_api.service.UserService;

import java.util.List;

@Controller
@RequestMapping("v1/users/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public ResponseEntity<List<LocalUser>> getUsers() {
        return userService.getUsers();
    }

    @GetMapping()
    public ResponseEntity<LocalUser> getUserById(@RequestParam Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("me")
    public ResponseEntity<LocalUser> getUser(@AuthenticationPrincipal LocalUser user) {
        return userService.getUserById(user.getId());
    }

    @PutMapping("update")
    public ResponseEntity<String> updateUser(@RequestBody LocalUser updatedUser) {
        return userService.updateUser(updatedUser);
    }


    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal LocalUser user, @RequestParam Long id) {
        if (user.getUserType().equals(UserType.TEACHER) || user.getUserType().equals(UserType.ADMIN) || user.getId().equals(id)) {
            return userService.deleteUser(id);
        }
        return ResponseEntity.badRequest().body("You are not authorized to delete this user");
    }
}
