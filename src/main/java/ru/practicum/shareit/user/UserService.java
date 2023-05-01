package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    void deleteUserById(int id);

    UserDto updateUser(User user, int id);

    UserDto createUser(User user);
}
