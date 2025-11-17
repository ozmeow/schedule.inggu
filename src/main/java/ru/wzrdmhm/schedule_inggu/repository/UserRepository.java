package ru.wzrdmhm.schedule_inggu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wzrdmhm.schedule_inggu.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);

    boolean existsByTelegramId(Long telegramId);

    List<User> findByGroupName(String groupName);
}
