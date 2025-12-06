package ru.wzrdmhm.schedule_inggu.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.entity.Group;
import ru.wzrdmhm.schedule_inggu.model.entity.User;
import ru.wzrdmhm.schedule_inggu.model.UserState;
import ru.wzrdmhm.schedule_inggu.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Map<Long, User> usersCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<User> allUsers = userRepository.findAll();
        allUsers.forEach(user -> usersCache.put(user.getTelegramId(), user));
        System.out.println("✅ Загружено пользователей из БД в кэш: " + usersCache.size());
    }

    public User findOrCreateUser(Long telegramId) {
        return findOrCreateUser(telegramId, "User");
    }

    public User findOrCreateUser(Long telegramId, String firstName) {
        User cachedUser = usersCache.get(telegramId);

        if (cachedUser != null) {
            return cachedUser;
        }

        Optional<User> dbUser = userRepository.findByTelegramId(telegramId);
        if (dbUser.isPresent()) {
            User user = dbUser.get();
            usersCache.put(telegramId, user);
            return user;
        }

        User newUser = new User();
        newUser.setTelegramId(telegramId);
        newUser.setFirstName(firstName != null ? firstName : "User");
        newUser.setState(UserState.START);

        User savedUser = userRepository.save(newUser);
        usersCache.put(telegramId, savedUser);
        System.out.println("✅ Создан новый пользователь в БД: " + telegramId);

        return savedUser;
    }

    public void setUserGroup(Long userId, Group group) {
        User user = findOrCreateUser(userId);
        user.setGroup(group);
        userRepository.save(user);
    }

    public Group getUserGroup(Long telegramId) {
        User user = usersCache.get(telegramId);

        if (user == null) {
            user = findOrCreateUser(telegramId, "User");
        }
        return user.getGroup();
    }

    public void validateUserHasGroup(Long userId) {
        try {
            Group group = getUserGroup(userId);
            String groupName = group.toString();
            if (group == null || group.equals("GROUP_NOT_SET") || groupName.trim().isEmpty()) {
                throw new UserGroupNotSetException("Сначала выберите группу");
            }
        } catch (RuntimeException e) {
            if (e instanceof UserGroupNotSetException) {
                throw e;
            }
            throw new UserGroupNotSetException("❌ Пользователь не найден validateUserHasGroup.");
        }
    }


    public class UserGroupNotSetException extends RuntimeException {
        public UserGroupNotSetException(String message) {
            super(message);
        }
    }
}

