package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteUserById(Long id);

    UserDto updateUser(User user, Long id);

    UserDto createUser(User user);
}
