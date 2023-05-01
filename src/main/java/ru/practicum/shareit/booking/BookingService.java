package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(InputBookingDto inputBookingDto, Long ownerId);

    BookingDto updateBooking(Long bookerId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long id);

    List<BookingDto> getBookingByOwnerId(Long bookerId, String state);

    ShortBookingDto getNextBooking(Long itemId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state);

    ShortBookingDto getLastBooking(Long itemId);
    Booking getBookedItemWithBooker(Long itemId, Long userId);
}
