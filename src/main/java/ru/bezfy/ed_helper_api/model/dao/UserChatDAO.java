package ru.bezfy.ed_helper_api.model.dao;

import org.springframework.data.repository.ListCrudRepository;
import ru.bezfy.ed_helper_api.model.UserChat;

import java.util.List;

public interface UserChatDAO extends ListCrudRepository<UserChat, Long> {
    List<UserChat> findByUserId(Long userId);
    List<UserChat> findByUserIdOrderByCreatedAtDesc(Long userId);
}
