package ru.bezfy.ed_helper_api.api.controller.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.Transaction;
import ru.bezfy.ed_helper_api.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("v1/transactions/")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("by_user")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@AuthenticationPrincipal LocalUser authUser) {
        return transactionService.getTransactionByUser(authUser);
    }

    @PostMapping("create")
    public ResponseEntity<String> createTransaction(@AuthenticationPrincipal LocalUser user, @RequestBody Transaction transaction) {
        return transactionService.createTransaction(user, transaction);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam Long id) {
        return transactionService.deleteTransaction(id);
    }

}
