package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.LocalUser;

import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
    Optional<LocalUser> findByEmail(String email);
}
