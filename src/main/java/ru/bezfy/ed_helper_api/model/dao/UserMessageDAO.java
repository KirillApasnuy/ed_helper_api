package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.UserMessage;

import java.util.List;

public interface UserMessageDAO extends ListCrudRepository<UserMessage, Long> {
    List<UserMessage> findByUserChatIdOrderByTimestampAsc(Long chatId);
}
