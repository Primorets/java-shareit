package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    @Autowired
    private BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable(value = "bookingId") Long bookingId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос на получение предмета по ID: " + bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwnerById(@RequestParam(name = "state",defaultValue = "ALL") String state,
                                                      @RequestHeader(USER_ID) Long ownerId ) {
        log.info("Получен запрос на получение всех предметов.");
        return bookingService.getBookingsByOwnerId(ownerId,state);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerId(@RequestParam(name = "state",defaultValue = "ALL") String state,
                                                     @RequestHeader(USER_ID) Long bookerId ) {
        log.info("Получен запрос на получение всех предметов.");
        return bookingService.getBookingsByBookerId(bookerId, state);
    }

    @ResponseBody
    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody InputBookingDto inputBookingDto,
                              @RequestHeader(USER_ID) Long bookerId) {
        log.info("Добавлен предмет : " + inputBookingDto + " пользователем c ID: " + bookerId);
        return bookingService.createBooking(inputBookingDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(USER_ID) Long bookerId, @PathVariable Long bookingId,
                                    @RequestParam Boolean approved) {
        log.info("Получен запрос на изменение данных предмета с ID: " + bookingId);
        return bookingService.updateBooking(bookerId, bookingId, approved);
    }
}
