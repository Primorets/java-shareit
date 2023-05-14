package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRequestServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    private UserDto userDto1 = new UserDto(1L, "kamen",
            "kamen@gmail.com");

    private UserDto userDto2 = new UserDto(2L, "kamen2",
            "kamen2@gmail.com");

    private ItemRequestDto itemRequestDto1 = new ItemRequestDto(1L, "desc",
            UserMapper.toUser(userDto1), LocalDateTime.now(), null);

    private ItemRequestDto itemRequestDto2 = new ItemRequestDto(2L, "desc",
            UserMapper.toUser(userDto2), LocalDateTime.now(), null);


    @Test
    void createItemRequest() {
        UserDto userDto2 = userService.createUser(this.userDto2);
        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now());
        assertThat(itemRequestDto1.getDescription(), equalTo(itemRequestDto2.getDescription()));
    }

    @Test
    void createWithoutUser() {
        UserNotFoundException userNotFoundException
                = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto2, 20L, LocalDateTime.now()));
        assertEquals("Пользователь не был зарегестрирован.", userNotFoundException.getMessage());
    }

    @Test
    void getWithIdExp() {
        UserDto userDto2 = userService.createUser(this.userDto2);
        ItemRequestNotFoundException itemRequestNotFoundException
                = assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestService.getItemRequestById(20L, 1L));
        assertEquals("Запрос не найден.", itemRequestNotFoundException.getMessage());
    }

    @Test
    void getItemRequestById() {
        UserDto userDto2 = userService.createUser(this.userDto2);
        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now());
        assertThat(itemRequestDto1.getDescription(),
                equalTo(itemRequestService.getItemRequestById(1L, 1L).getDescription()));
    }

    @Test
    void getAllOwnerItemRequests() {
        UserDto userDto2 = userService.createUser(this.userDto2);
        ItemRequestDto newItemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now());
        ItemRequestDto newItemRequestDto2 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now());
        ItemRequestDto newItemRequestDto3 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now());
        List<ItemRequestDto> list = itemRequestService.getAllOwnerItemRequests(userDto2.getId());

        assertThat(list.size(), equalTo(3));
    }

    @Test
    void getAllItemRequests() {
        UserDto newUserDto1 = userService.createUser(userDto1);
        UserDto newUserDto2 = userService.createUser(userDto2);
        ItemRequestDto newItemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto1, userDto1.getId(),
                LocalDateTime.now().plusHours(1));
        ItemRequestDto newItemRequestDto2 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now().plusHours(3));
        ItemRequestDto newItemRequestDto3 = itemRequestService.createItemRequest(itemRequestDto2, userDto2.getId(),
                LocalDateTime.now().plusHours(6));
        List<ItemRequestDto> list = itemRequestService.getAllItemRequests(userDto1.getId(), 10, 20);

        assertThat(list.size(), equalTo(2));

    }

    @Test
    void getAllItemRequestsWithWrongFrom() {
        UserDto userDto3 = userService.createUser(userDto2);
        ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequests(1L, 0, 0));
        assertEquals("Не правильные параметры пангинации. ", validationException.getMessage());
    }


}