package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.Transaction;

import java.util.List;

public interface TransactionDAO extends ListCrudRepository<Transaction, Long> {
    List<Transaction> findAllByUser(LocalUser user);
}
