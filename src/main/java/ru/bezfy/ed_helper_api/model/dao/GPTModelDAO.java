package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.GPTModel;

import java.util.Optional;


public interface GPTModelDAO extends ListCrudRepository<GPTModel, Long> {
    Optional<GPTModel> findByName(String name);
}
