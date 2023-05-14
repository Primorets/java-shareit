package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    @Lazy
    private ItemMapper itemMapper;

    @Autowired
    @Lazy
    private BookingMapper bookingMapper;
    private ItemDto itemDto;
    private UserDto owner;

    private BookingDto bookingDto;

    private UserDto booker;

    @BeforeEach
    void init() throws UserNotFoundException, ItemNotFoundException {
        owner = userService.createUser(new UserDto(null, "kamen", "kamen@gmail.com"));
        booker = userService.createUser(new UserDto(null, "kamen1", "kamen1@gmail.com"));
        itemDto = itemService.createItem(new ItemDto(null, "lom", "black", true, UserMapper.toUser(owner), null, null, null, null), owner.getId());
        bookingDto = bookingService.createBooking(new InputBookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1)), 2L);

    }

    @Test
    void createBookingTest() {
        BookingDto booking2 = bookingService.createBooking(new InputBookingDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)), booker.getId());
        assertEquals(booking2.getId(), 2L);
    }

    @Test
    void updateBookingTest() throws UserNotFoundException, ItemNotFoundException, BookingNotFoundException {
        BookingDto bookingDto1 = bookingService.updateBooking(1L, 1L, true);
        assertEquals(Status.APPROVED, bookingDto1.getStatus());
    }


    @Test
    void getBookingByIdTest() {
        BookingDto booking2 = bookingService.createBooking(new InputBookingDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)), booker.getId());
        assertEquals(booking2, bookingService.getBookingById(2L, 1L));
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getBookingById(5L, 1L);
                    }
                });
        assertEquals("Бронирование не найдено.", exception.getMessage());
        UserNotFoundException exception1 = assertThrows(UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getBookingById(1L, 3L);
                    }
                });
        assertEquals("Пользователь не был зарегестрирован.", exception1.getMessage());

        UserDto unBooker = userService.createUser(new UserDto(null, "kamen2", "kamen2@gmail.com"));

        UserNotFoundException exception2 = assertThrows(UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getBookingById(2L, 3L);
                    }
                });
        assertEquals(" Проверить данные бронирования может только владелец вещи. ", exception2.getMessage());
    }

    @Test
    void getBookingsByBookerIdTest() {
        BookingDto booking2 = bookingService.createBooking(new InputBookingDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)), booker.getId());
        assertEquals(List.of(booking2), bookingService.getBookingsByBookerId(booker.getId(), "WAITING", 0, 1));
        assertEquals(new ArrayList<>(), bookingService.getBookingsByBookerId(booker.getId(), "REJECTED", 0, 1));
        assertEquals(new ArrayList<>(), bookingService.getBookingsByBookerId(booker.getId(), "PAST", 0, 1));
        assertEquals(List.of(booking2), bookingService.getBookingsByBookerId(booker.getId(), "FUTURE", 0, 1));
        assertEquals(List.of(bookingDto), bookingService.getBookingsByBookerId(booker.getId(), "CURRENT", 0, 1));
        assertEquals(List.of(booking2), bookingService.getBookingsByBookerId(booker.getId(), "ALL", 0, 1));
        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getBookingsByOwnerId(booker.getId(), "PPPP", 0, 1);
                    }
                });
        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }

    @Test
    void getBookingsByOwnerIdTest() throws ValidationException {
        BookingDto booking2 = bookingService.createBooking(new InputBookingDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)), booker.getId());
        assertEquals(List.of(booking2), bookingService.getBookingsByOwnerId(owner.getId(), "WAITING", 0, 1));
        assertEquals(new ArrayList<>(), bookingService.getBookingsByOwnerId(owner.getId(), "REJECTED", 0, 1));
        assertEquals(new ArrayList<>(), bookingService.getBookingsByOwnerId(owner.getId(), "PAST", 0, 1));
        assertEquals(List.of(booking2), bookingService.getBookingsByOwnerId(owner.getId(), "FUTURE", 0, 1));
        assertEquals(List.of(bookingDto), bookingService.getBookingsByOwnerId(owner.getId(), "CURRENT", 0, 1));
        assertEquals(List.of(booking2), bookingService.getBookingsByOwnerId(owner.getId(), "ALL", 0, 1));
        ValidationException exception = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getBookingsByOwnerId(owner.getId(), "PPPP", 0, 1);
                    }
                });
        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}