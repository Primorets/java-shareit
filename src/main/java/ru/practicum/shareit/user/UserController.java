package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public UserDto getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Validated(Create.class) User user){
        return userService.createUser(user);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody @Validated(Update.class) User user, @PathVariable int id){
        return userService.updateUser(user);
    }

    @DeleteMapping
    public UserDto deleteUserById(@PathVariable int id){
        return userService.deleteUserById(id);
    }


}
