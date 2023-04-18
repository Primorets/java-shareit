package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserRepository implements UserRepository {

    private HashMap<Integer, User> usersMap;
    private int userId;

    public InMemoryUserRepository() {
        usersMap = new HashMap<>();
        userId = 0;
    }

    @Override
    public User getUserById(int id) {
        checkId(id);
        return usersMap.get(id);
    }

    @Override
    public void deleteUserById(int id) {
        checkId(id);
        usersMap.remove(id);
    }

    @Override
    public User updateUser(User user, int id) {
        user.setId(id);
        checkId(id);
        if (user.getEmail() == null) {
            user.setEmail(usersMap.get(user.getId()).getEmail());
        }
        if (user.getName() == null) {
            user.setName(usersMap.get(user.getId()).getName());
        }
        if (checkUniqueEmail(user)) {
            usersMap.put(user.getId(), user);
        }
        return usersMap.get(user.getId());
    }

    @Override
    public User saveUser(User user) {
        checkUniqueEmail(user);
        user.setId(++userId);
        usersMap.put(user.getId(), user);
        return usersMap.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    private boolean checkUniqueEmail(User user) {
        if (usersMap.containsKey(user.getId())) {
            if (usersMap.values().stream()
                    .filter(user1 -> user1.getId() != user.getId())
                    .anyMatch(user1 -> user1.getEmail()
                            .equals(user.getEmail()))) {
                throw new DuplicateEmailException("Email уже зарегестрирован");

            }
        } else if (usersMap.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new DuplicateEmailException("Email уже зарегестрирован");
        }
        return true;
    }

    private void checkId(int id) {
        if (!usersMap.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с данным ID не существует");
        }
    }
}
