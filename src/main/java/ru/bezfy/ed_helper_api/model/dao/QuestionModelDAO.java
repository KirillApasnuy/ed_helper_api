package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.QuestionModel;

import java.util.List;
import java.util.Optional;

public interface QuestionModelDAO extends ListCrudRepository<QuestionModel, Long> {
    Optional<List<QuestionModel>> findAllByUserId(Long userId);
}
