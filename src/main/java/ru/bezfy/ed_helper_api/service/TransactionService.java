package ru.bezfy.ed_helper_api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.Transaction;
import ru.bezfy.ed_helper_api.model.dao.TransactionDAO;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(transactionDAO.findAll());
    }

    public ResponseEntity<List<Transaction>> getTransactionByUser(LocalUser user) {
        return ResponseEntity.ok(transactionDAO.findAllByUser(user));
    }

    public ResponseEntity<String> createTransaction(LocalUser user, Transaction transaction) {
        transaction.setUser(user);
        transactionDAO.save(transaction);
        return ResponseEntity.ok("Transaction created");
    }

    public ResponseEntity<String> deleteTransaction(Long id) {
        if (!transactionDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transactionDAO.deleteById(id);
        return ResponseEntity.ok("Transaction deleted");
    }
}
