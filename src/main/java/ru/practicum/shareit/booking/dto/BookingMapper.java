package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Component
public class BookingMapper {


    @Autowired
    @Lazy
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private ItemMapper itemMapper;


    public BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            return new BookingDto(booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    itemMapper.toItemDto(booking.getItem()),
                    UserMapper.toUserDto(booking.getBooker()),
                    booking.getStatus());
        } else {
            return null;
        }
    }

    public ShortBookingDto shortBookingDto(Booking booking){
        if (booking!=null){
            return new ShortBookingDto(booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd());
        } else {
            return null;
        }
    }
    public Booking toBooking(InputBookingDto inputBookingDto,Long bookerId){
        return new Booking(null,
                inputBookingDto.getStart(),
                inputBookingDto.getEnd(),
                itemService.getItemByIdForBookingMapper(inputBookingDto.getItemId()),
                userService.getUserForBookingMapper(bookerId),
                Status.WAITING);
    }
}
