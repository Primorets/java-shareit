package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        log.info("Получен запрос на получение пользователя по ID: " + id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей.");
        return userService.getAllUsers();
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@RequestBody @Validated(Create.class) User user) {
        log.info("Добавлен пользователь: " + user);
        return userService.createUser(user);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody @Validated(Update.class) User user, @PathVariable int id) {
        log.info("Получен запрос на изменение данных о пользователе с ID: " + id);
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя с ID: " + id);
        userService.deleteUserById(id);
    }
}
