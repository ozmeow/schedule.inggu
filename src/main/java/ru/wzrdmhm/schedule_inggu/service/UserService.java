package ru.wzrdmhm.schedule_inggu.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.User;
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
        newUser.setGroupName("ХББ");
        newUser.setState(UserState.START);

        User savedUser = userRepository.save(newUser);
        usersCache.put(telegramId, savedUser);
        System.out.println("✅ Создан новый пользователь в БД: " + telegramId);

        return savedUser;
    }

    public synchronized void setUserGroup(Long telegramId, String groupName) {
        User user = findOrCreateUser(telegramId, "User");
        user.setGroupName(groupName);
        userRepository.save(user);
        usersCache.put(telegramId, user);
    }

    public String getUserGroup(Long telegramId) {
        User user = usersCache.get(telegramId);
        if (user == null) {
            user = findOrCreateUser(telegramId, "User");
        }
        return user.getGroupName();
    }

    public void validateUserHasGroup(Long userId) {
        try {
            String group = getUserGroup(userId);
            if (group == null || group.equals("GROUP_NOT_SET") || group.trim().isEmpty()) {
                throw new UserGroupNotSetException("Сначала установите группу с помощью: " +
                        "/setgroup название группы");
            }
        } catch (RuntimeException e) {
            if (e instanceof UserGroupNotSetException) {
                throw e;
            }
            throw new UserGroupNotSetException("❌ Пользователь не найден. Используйте: /start");
        }
    }


    public class UserGroupNotSetException extends RuntimeException {
        public UserGroupNotSetException(String message) {
            super(message);
        }
    }
}

