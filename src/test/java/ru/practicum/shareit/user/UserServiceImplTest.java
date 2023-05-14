package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    private UserDto user;
    private UserDto user2;

    @BeforeEach
    void init() throws UserNotFoundException, ItemNotFoundException {
        user = userService.createUser(new UserDto(null, "kamen", "kamen@gmail.com"));
        user2 = new UserDto(null, "kamen1", "kamen1@gmail.com");

    }

    @Test
    void getUserById() {
        assertEquals(userService.getUserById(1L).getName(), "kamen");
    }

    @Test
    void getAllUsers() {
        User user1 = UserMapper.toUser(userService.createUser(user2));
        assertEquals(userService.getAllUsers().size(), 2);
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(1L);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.getUserById(1L);
                    }
                });
        assertEquals("Пользователь не был зарегестрирован.", exception.getMessage());
    }

    @Test
    void updateUser() {
        user = userService.updateUser(new UserDto(null, "kamenUpd", "kamen3@gmail.com"), 1L);
        UserDto userDto = userService.createUser(user2);
        assertEquals(user.getName(), "kamenUpd");
        assertEquals(user.getEmail(), "kamen3@gmail.com");
        UserDto userDto2 = userService.createUser(new UserDto(null,"name", "g@maiol.ru"));
        user = userService.updateUser(new UserDto(null, "kamenUpd2", null), 1L);
        assertEquals(user.getName(), "kamenUpd2");
        user = userService.updateUser(new UserDto(null, null, "kamen5@gmail.com"), 1L);
        assertEquals(user.getEmail(), "kamen5@gmail.com");
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(new UserDto(133L,"gfjh","fdgd@sg"),133L));
        assertEquals("Пользователь не был зарегестрирован.",
                exception.getMessage());
    }

    @Test
    void shouldExceptionWhenUpdateUserWithExistEmail() {
        User user3 = new User(3L, "User3", "user3@m.ru");
        userService.createUser(UserMapper.toUserDto(user3));
        User newUser = new User(4L, "User4", "user4@m.ru");
        UserDto returnUserDto = userService.createUser(UserMapper.toUserDto(newUser));
        Long id = returnUserDto.getId();
        returnUserDto.setId(null);
        returnUserDto.setEmail("user3@m.ru");
        final DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> userService.updateUser(returnUserDto, id));
        assertEquals("Email уже зарегестрирован",
                exception.getMessage());
    }

    @Test
    void createUser() {
        User user1 = UserMapper.toUser(userService.createUser(user2));
        assertEquals(user1.getId(), 2L);
        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.createUser(new UserDto(null, " ", "kamen5@gmail.com"));
                    }
                });
        assertEquals("Введено пустое имя", exception.getMessage());
        ValidationException exception2 = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.createUser(new UserDto(null, "jklh", "kamen5gmail.com"));
                    }
                });
        assertEquals("Введён не правильный email", exception2.getMessage());
    }

    @Test
    void validTest() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.getUserForBookingMapper(6L);
                    }
                });
        assertEquals("Пользователь не был зарегестрирован.", exception.getMessage());

    }
}