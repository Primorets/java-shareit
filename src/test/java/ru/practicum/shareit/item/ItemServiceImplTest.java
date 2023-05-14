package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceImplTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserService userService;
    @Autowired
    @Lazy
    private ItemService itemService;

    private User user = new User(1L, "kamen",
            "kamen@gmail.com");

    private UserDto userDto1 = new UserDto(2L, "kamen2",
            "kamen2@gmail.com");

    private UserDto userDto2 = new UserDto(3L, "kamen3",
            "kamen3@gmail.com");

    private ItemDto itemDto = new ItemDto(1L, "lom", "black",
            true, user,
            null, null, null, null);
    private ItemDto itemDto2 = new ItemDto(2L, "molotok", "black2",
            true, user,
            null, null, null, null);

    @Test
    void createItem() {
        UserDto newUser = userService.createUser(userDto1);
        ItemDto newItem = itemService.createItem(itemDto, newUser.getId());
        ItemDto getItem = itemService.getItemById(newItem.getId(), newUser.getId());
        assertThat(getItem.getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void createItemNotValidItem() {
        UserDto newUser = userService.createUser(userDto1);
        ItemDto newItem = new ItemDto(1L, null, "black",
                true, user,
                null, null, null, null);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> itemService.createItem(newItem, newUser.getId()));
        assertEquals("Ошибка в теле запроса. Отсутствует имя. ", validationException.getMessage());
        ItemDto newItem2 = new ItemDto(1L, "es", null,
                true, user,
                null, null, null, null);
        ValidationException validationException2 = assertThrows(ValidationException.class,
                () -> itemService.createItem(newItem2, newUser.getId()));
        assertEquals("Ошибка в теле запроса. Отсутствует описание. ", validationException2.getMessage());
        ItemDto newItem3 = new ItemDto(1L, "es", "black",
                null, user,
                null, null, null, null);
        ValidationException validationException3 = assertThrows(ValidationException.class,
                () -> itemService.createItem(newItem3, newUser.getId()));
        assertEquals("Ошибка в теле запроса. Отсутствует статус.", validationException3.getMessage());
    }

    @Test
    void updateItem() {
        UserDto newUserDto = userService.createUser(userDto1);
        ItemDto newItemDto = itemService.createItem(itemDto, newUserDto.getId());
        newItemDto.setName("updName");
        newItemDto.setDescription("updDescription");
        newItemDto.setAvailable(false);
        ItemDto returnItemDto = itemService.updateItem(newUserDto.getId(), newItemDto, newItemDto.getId());
        assertThat(returnItemDto.getName(), equalTo("updName"));
        assertThat(returnItemDto.getDescription(), equalTo("updDescription"));
        assertFalse(returnItemDto.getAvailable());
    }

    @Test
    void updateItemWithoutOwner() {
        UserDto owner = userService.createUser(userDto1);
        UserDto userUpd = userService.createUser(userDto2);
        ItemDto newItemDto = itemService.createItem(itemDto, owner.getId());
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> itemService.updateItem(userUpd.getId(), newItemDto, itemDto.getId()));
        assertEquals("Предмет добавлен другим пользователем.", userNotFoundException.getMessage());
    }

    @Test
    void getItemById() {
        UserDto owner = userService.createUser(userDto1);
        ItemDto newItemDto = itemService.createItem(itemDto, owner.getId());
        ItemDto newItemDto2 = itemService.createItem(itemDto2, owner.getId());

        assertEquals("lom", itemService.getItemById(newItemDto.getId(), owner.getId()).getName());
    }

    @Test
    void searchItem() {
        UserDto owner = userService.createUser(userDto1);
        ItemDto newItemDto = itemService.createItem(itemDto, owner.getId());
        ItemDto newItemDto2 = itemService.createItem(itemDto2, owner.getId());

        List<ItemDto> itemDtoList = itemService.searchItem("lom", 0, 10);
        assertEquals(1, itemDtoList.size());
    }

    @Test
    void deleteItemById() {
        UserDto newUserDto = userService.createUser(userDto1);
        ItemDto newItemDto = itemService.createItem(itemDto, newUserDto.getId());
        itemService.deleteItemById(newItemDto.getId());

        ItemNotFoundException itemNotFoundException = assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemById(newItemDto.getId(), newUserDto.getId()));
        assertEquals("Предмет не найден.", itemNotFoundException.getMessage());
    }

    @Test
    void getItemsByOwnerId() {
        UserDto owner = userService.createUser(userDto1);
        ItemDto newItemDto = itemService.createItem(itemDto, owner.getId());
        ItemDto newItemDto2 = itemService.createItem(itemDto2, owner.getId());

        List<ItemDto> itemDtoList = itemService.getItemsByOwnerId(owner.getId(), 0, 10);
        assertEquals(2, itemDtoList.size());


    }

    @Test
    void createComment() {
        UserDto owner = userService.createUser(userDto1);
        UserDto userUpd = userService.createUser(userDto2);
        ItemDto newItemDto = itemService.createItem(itemDto, owner.getId());
        InputBookingDto bookingDto = new InputBookingDto(
                newItemDto.getId(),
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(3)
        );
        BookingDto bookingDto1 = bookingService.createBooking(bookingDto, userUpd.getId());
        bookingService.updateBooking(owner.getId(), bookingDto1.getId(), true);
        try {
            sleep(5000);
        } catch (InterruptedException runtimeException) {
            throw new RuntimeException(runtimeException);
        }
        CommentDto commentDto = new CommentDto(1L, "comment1",
                itemMapper.toItem(newItemDto, UserMapper.toUser(owner)),
                userUpd.getName(), LocalDateTime.now().plusHours(5));
        itemService.createComment(commentDto, newItemDto.getId(), userUpd.getId());
        assertEquals(1, itemService.getCommentsToIemByItemId(newItemDto.getId()).size());
    }
}