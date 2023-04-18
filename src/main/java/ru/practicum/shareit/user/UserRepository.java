package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User getUserById(int id);

    void deleteUserById(int id);

    User updateUser(User user, int id);

    User saveUser(User user);

    List<User> getAllUsers();
}
