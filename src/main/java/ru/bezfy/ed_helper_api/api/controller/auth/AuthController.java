package ru.bezfy.ed_helper_api.api.controller.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.api.model.AuthorizationModel;
import ru.bezfy.ed_helper_api.exception.EmailFailureException;
import ru.bezfy.ed_helper_api.exception.InvalidCredentialsException;
import ru.bezfy.ed_helper_api.exception.UserAlreadyExistsException;
import ru.bezfy.ed_helper_api.exception.UserNotFoundException;
import ru.bezfy.ed_helper_api.service.AuthService;

@Controller
@RequestMapping("v1/auth/")
public class AuthController {
    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    ResponseEntity<String> register(@RequestBody AuthorizationModel authModel) {
        if (authModel != null) {
            try {
                authService.register(authModel);
                return ResponseEntity.ok("Success");
            } catch (UserAlreadyExistsException e) {
                return ResponseEntity.badRequest().body("User already exists");
            } catch (EmailFailureException e) {
                return ResponseEntity.badRequest().body("Verfication token failed sending");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("login")
    ResponseEntity<String> login(@Valid @RequestBody AuthorizationModel authModel) {
        if (authModel != null) {
            try {
                return ResponseEntity.ok(authService.login(authModel));
            } catch (UserNotFoundException e) {
                return ResponseEntity.badRequest().body("User not found");
            } catch (InvalidCredentialsException e) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("verify")
    ResponseEntity<String> verify(@RequestParam String token) {
        if (token != null) {
            try {
                return ResponseEntity.ok(authService.verifyUser(token));
            } catch (InvalidCredentialsException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
