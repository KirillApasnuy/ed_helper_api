package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.Subscription;

import java.util.Optional;

public interface SubscriptionDAO extends ListCrudRepository<Subscription, Long> {
    Optional<Subscription> findById(Long id);
}
