package ru.bezfy.ed_helper_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.api.model.SubscriptionBody;
import ru.bezfy.ed_helper_api.exception.EmailFailureException;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.Subscription;
import ru.bezfy.ed_helper_api.model.Transaction;
import ru.bezfy.ed_helper_api.model.dao.LocalUserDAO;
import ru.bezfy.ed_helper_api.model.dao.SubscriptionDAO;
import ru.bezfy.ed_helper_api.model.enums.SubscribeState;

import java.util.*;

@Service
public class SubscriptionService {
    private final SubscriptionDAO subscriptionDAO;
    private final LocalUserDAO localUserDAO;
    private final TransactionService transactionService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    public SubscriptionService(SubscriptionDAO subscriptionDAO, LocalUserDAO localUserDAO, TransactionService transactionService, EmailService emailService) {
        this.subscriptionDAO = subscriptionDAO;
        this.localUserDAO = localUserDAO;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    public ResponseEntity<List<Subscription>> getSubscriptions() {
        return ResponseEntity.ok(subscriptionDAO.findAll());
    }

    public ResponseEntity<?> addSubscription(SubscriptionBody subscription) {
        if (subscription == null) {
            return ResponseEntity.badRequest().body("Subscription data is missing");
        }

        logger.info("Received new subscription: {}", subscription);

        Subscription newSubscription = new Subscription(
                subscription.getRuTitle(),
                subscription.getEnTitle(),
                subscription.isPremium(),
                subscription.getLimitGenerations(),
                subscription.isTTS(),
                subscription.getRuBenefits(),
                subscription.getEnBenefits(),
                subscription.getAmountPerMonth(),
                subscription.getAmountPerMonthInYear(),
                subscription.isAccessInGroup()
        );

        subscriptionDAO.save(newSubscription);
        return ResponseEntity.ok(newSubscription);
    }

    public ResponseEntity<String> subscribeUser(LocalUser user, Long id, Boolean isAutoRenewal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Optional<Subscription> opSubscription = subscriptionDAO.findById(id);
        if (opSubscription.isPresent()) {
            Subscription subscription = opSubscription.get();
            user.setSubscription(subscription);
            user.setPaidStartDate(new Date());
            calendar.add(Calendar.MONTH, 1);
            user.setPaidEndDate(calendar.getTime());
            user.setAutoRenewal(isAutoRenewal);
            user.setSubscribeState(SubscribeState.SUBSCRIBED);
            localUserDAO.save(user);
            createTransaction(user, subscription);
            if (subscription.isAccessInGroup()) {
                try {
                    emailService.sendAccessInGroupEmail(user);
                } catch (EmailFailureException e) {
                    return ResponseEntity.badRequest().body("Email sending fail, user subscription");
                }
            }
            return ResponseEntity.ok("User subscribed");
        }
        return ResponseEntity.badRequest().body("Subscription not found");
    }

    public ResponseEntity<String> unSubscribeUser(LocalUser user) {
        if (user.getSubscription() == null) {
            return ResponseEntity.badRequest().body("User has no subscription");
        }
        user.setSubscription(null);
        user.setSubscribeState(SubscribeState.UNSUBSCRIBED);
        user.setAutoRenewal(false);
        user.setPaidStartDate(null);
        user.setPaidEndDate(null);
        localUserDAO.save(user);
        return ResponseEntity.ok("User unsubscribed");
    }

    public ResponseEntity<String> deleteSubscription(Long id) {
        if (subscriptionDAO.existsById(id)) {
            subscriptionDAO.deleteById(id);
            return ResponseEntity.ok("Subscription deleted");
        }
        return ResponseEntity.notFound().build();
    }

    private void createTransaction(LocalUser user, Subscription subscription) {

        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setUser(user);
        transaction.setAmount(subscription.getAmountPerMonth());
        transaction.setName("Payment for subscription " + subscription.getEnTitle());

        transactionService.createTransaction(user, transaction);
    }
}
