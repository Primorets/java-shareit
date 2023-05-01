package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;

    private ItemService itemService;

    @Autowired
    @Lazy
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.itemService = itemService;
    }

    @Override
    public BookingDto createBooking(InputBookingDto inputBookingDto, Long bookerId) {
        if (!itemService.getItemById(inputBookingDto.getId()).getAvailable()) {
            throw new ValidationException("");
        }
        Booking booking = bookingMapper.toBooking(inputBookingDto, bookerId);
        if (bookerId.equals(booking.getItem().getOwner().getId())) {
            throw new BookingNotFoundException("");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(Long bookerId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(""));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("");
        }

        if (booking.getBooker().getId().equals(bookerId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
            } else {
                throw new UserNotFoundException("");
            }
        } else if (bookerId.equals(booking.getItem().getOwner().getId())&&!booking.getStatus().equals(Status.CANCELED)){
            if (!booking.getStatus().equals(Status.WAITING)){
                throw new ValidationException("");
            }
            if (approved){
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }

        }else {
            if (booking.getStatus().equals(Status.CANCELED)){
                throw new ValidationException("");
            }else {
                throw new ValidationException("");
            }
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(""));
        if (booking.getBooker().getId().equals(id) || itemService.getItemsByOwnerId(id).stream().anyMatch(itemDto -> itemDto.getId().equals(booking.getItem().getId()))) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException("");
        }
    }

    @Override
    public List<BookingDto> getBookingByOwnerId(Long bookerId, String state) {
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBooker_Id(bookerId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository
                        .findByBooker_IdAndStartBeforeAndEndAfter(bookerId,
                                LocalDateTime.now(), LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findByBooker_IdAndEndBefore(bookerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBooker_IdAndStartAfter(bookerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBooker_IdAndStatus(bookerId, Status.WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBooker_IdAndStatus(bookerId, Status.REJECTED, sortByStartDesc);
                break;
            default:
                throw new ValidationException("Не известный ");
        }
        return bookings.stream().map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long bookerId, String state) {
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_Id(bookerId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_IdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndBefore(bookerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_Owner_IdAndStartAfter(bookerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(bookerId, Status.WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(bookerId, Status.REJECTED, sortByStartDesc);
                break;
            default:
                throw new ValidationException("Не известный ");
        }
        return bookings.stream().map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShortBookingDto getLastBooking(Long itemId) {
        ShortBookingDto shortBookingDto = bookingMapper.shortBookingDto(bookingRepository
                .findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now()));
        return shortBookingDto;
    }

    @Override
    public ShortBookingDto getNextBooking(Long itemId) {
        ShortBookingDto shortBookingDto = bookingMapper.shortBookingDto(bookingRepository
                .findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()));
        return shortBookingDto;
    }

    @Override
    public Booking getBookedItemWithBooker(Long itemId, Long userId) {
        return bookingRepository.findByItem_IdAndBooker_IdAndEndBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), Status.APPROVED);
    }
}
