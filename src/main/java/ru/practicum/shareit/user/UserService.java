package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


public interface UserService {
    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteUserById(Long id);

    UserDto updateUser(UserDto user, Long id);

    UserDto createUser(UserDto user);

    User getUserForBookingMapper(Long id);
}
