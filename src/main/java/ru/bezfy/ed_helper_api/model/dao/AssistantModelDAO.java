package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.AssistantModel;

import java.util.List;
import java.util.Optional;

public interface AssistantModelDAO extends ListCrudRepository<AssistantModel, Long> {
    Optional<AssistantModel> findById(Long id);

}
