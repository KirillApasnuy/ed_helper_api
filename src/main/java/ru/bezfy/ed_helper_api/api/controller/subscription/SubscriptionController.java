package ru.bezfy.ed_helper_api.api.controller.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.api.model.SubscriptionBody;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.Subscription;
import ru.bezfy.ed_helper_api.model.enums.UserType;
import ru.bezfy.ed_helper_api.service.SubscriptionService;

import java.util.List;

@Controller
@RequestMapping("v1/subscriptions/")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping()
    public ResponseEntity<List<Subscription>> getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @PostMapping("add")
    public ResponseEntity<?> addSubscription(@RequestBody SubscriptionBody subscription) {
        return subscriptionService.addSubscription(subscription);
    }

    @GetMapping("subscribe")
    public ResponseEntity<String> subscribeAssistant(@AuthenticationPrincipal LocalUser user, @RequestParam Long id, @RequestParam Boolean isAutoRenewal) {
        System.out.println(id + " " + isAutoRenewal);
        if (id == null) {
            return ResponseEntity.badRequest().body("Subscription id is null");
        }
        if (isAutoRenewal == null) {
            return ResponseEntity.badRequest().body("Auto renewal is null");
        }
        return subscriptionService.subscribeUser(user, id, isAutoRenewal);
    }

    @PostMapping("unsubscribe")
    public ResponseEntity<String> unSubscribeAssistant(@AuthenticationPrincipal LocalUser user) {
        return subscriptionService.unSubscribeUser(user);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteSubscription(@AuthenticationPrincipal LocalUser user, @RequestParam Long id) {
        if (user.getUserType().equals(UserType.TEACHER) || user.getUserType().equals(UserType.ADMIN)) {
            return subscriptionService.deleteSubscription(id);
        }
        return ResponseEntity.ok("You are not authorized to delete this assistant");
    }
}
