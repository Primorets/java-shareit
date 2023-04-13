package ru.practicum.shareit.user;

public interface UserService {
    UserDto getUserById(int id);

    UserDto deleteUserById(int id);

    UserDto updateUser(User user);

    UserDto createUser(User user);
}
