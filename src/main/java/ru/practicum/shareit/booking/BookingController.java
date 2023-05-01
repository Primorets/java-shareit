package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String BOOKER_ID = "X-Sharer-User-Id";

    @Autowired
    BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable(value = "bookingId") Long id) {
        log.info("Получен запрос на получение предмета по ID: " + id);
        return bookingService.getBookingById(id);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwnerById(@RequestParam(name = "state",defaultValue = "ALL") String state,
                                                      @RequestHeader(BOOKER_ID) Long bookerId ) {
        log.info("Получен запрос на получение всех предметов.");
        return bookingService.getBookingByOwnerId(bookerId,state);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestParam(name = "state",defaultValue = "ALL") String state,
                                           @RequestHeader(BOOKER_ID) Long bookerId ) {
        log.info("Получен запрос на получение всех предметов.");
        return bookingService.getBookingsByBookerId(bookerId, state);
    }

    @ResponseBody
    @PostMapping
    public BookingDto createBooking(@Validated({Create.class}) @RequestBody InputBookingDto inputBookingDto,
                              @RequestHeader(BOOKER_ID) Long bookerId) {
        log.info("Добавлен предмет : " + inputBookingDto + " пользователем c ID: " + bookerId);
        return bookingService.createBooking(inputBookingDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(BOOKER_ID) Long bookerId, @PathVariable Long bookingId,
                                    @RequestParam Boolean approved) {
        log.info("Получен запрос на изменение данных предмета с ID: " + bookingId);
        return bookingService.updateBooking(bookerId, bookingId, approved);
    }
}
