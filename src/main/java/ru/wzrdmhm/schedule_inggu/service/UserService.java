package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private Map<Long, User> users = new HashMap<>();

    public User findOrCreateUser(Long telegramId, String firstName) {
        if (users.containsKey(telegramId)) {
            return users.get(telegramId);
        }

        User newUser = new User();
        newUser.setTelegramId(telegramId);
        newUser.setFirstName(firstName);
        newUser.setGroupName("Bio-19");

        users.put(telegramId, newUser);
        return newUser;
    }

    public void setUserGroup(Long telegramId, String groupName) {
        if (!users.containsKey(telegramId)) {
            User newUser = new User();
            newUser.setTelegramId(telegramId);
            newUser.setGroupName(groupName);
            users.put(telegramId, newUser);
        } else {
            User existingUser = users.get(telegramId);
            existingUser.setGroupName(groupName);
        }
    }

    public String getUserGroup(Long telegramId) {
        if (!users.containsKey(telegramId)) {
            throw new RuntimeException("Пользователь не найден");
        }
        User user = users.get(telegramId);
        return user.getGroupName();
    }
}
