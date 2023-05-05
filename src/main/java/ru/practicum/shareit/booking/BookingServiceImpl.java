package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    @Lazy
    private ItemService itemService;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(InputBookingDto inputBookingDto, Long bookerId) {
        checkUserId(bookerId);
        if (!isAvailableItem(inputBookingDto.getItemId(), bookerId)) {
            throw new ValidationException("Item c ID: " + inputBookingDto.getItemId() + " недоступна для бронирования!");
        }
        checkTime(inputBookingDto.getStart(), inputBookingDto.getEnd());
        Booking booking = bookingMapper.toBooking(inputBookingDto, bookerId);
        if (bookerId.equals(booking.getItem().getOwner().getId())) {
            throw new BookingNotFoundException("Вещь не найдена!");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(Long bookerId, Long bookingId, Boolean approved) {
        checkUserId(bookerId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(""));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException(" ");
        }
        if (booking.getBooker().getId().equals(bookerId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
            } else {
                throw new UserNotFoundException("");
            }
        } else if (bookerId.equals(booking.getItem().getOwner().getId()) && !booking.getStatus().equals(Status.CANCELED)) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("");
            } else {
                throw new ValidationException("");
            }
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        checkUserId(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(""));
        if (booking.getBooker().getId().equals(userId) || itemService.getItemsByOwnerId(userId).stream()
                .anyMatch(itemDto -> itemDto.getId().equals(booking.getItem().getId()))) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException("");
        }
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long ownerId, String state) {
        checkUserId(ownerId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(ownerId);
                //bookings = bookingRepository.findByBooker_Id(ownerId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings=bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                    LocalDateTime.now(), LocalDateTime.now());
                //bookings = bookingRepository
                        //.findByBooker_IdAndStartBeforeAndEndAfter(ownerId,
                                //LocalDateTime.now(), LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
               // bookings = bookingRepository.findByBooker_IdAndEndBefore(ownerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
               // bookings = bookingRepository.findByBooker_IdAndStartAfter(ownerId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(ownerId, Status.WAITING);
                //bookings = bookingRepository.findByBooker_IdAndStatus(ownerId, Status.WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(ownerId, Status.REJECTED);
                //bookings = bookingRepository.findByBooker_IdAndStatus(ownerId, Status.REJECTED, sortByStartDesc);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(Long ownerId, String state) {
        checkUserId(ownerId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_IdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItemOwnerId(ownerId,LocalDateTime.now());
                //bookings = bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShortBookingDto getLastBooking(Long itemId) {
       // return bookingMapper.shortBookingDto(bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now()));
        return bookingMapper.shortBookingDto(bookingRepository.findAllByItem_IdAndStartBeforeOrderByStartDesc(itemId,LocalDateTime.now())
                .stream().min(Comparator.comparing(Booking::getEnd)).orElse(null));


    }

    @Override
    public ShortBookingDto getNextBooking(Long itemId) {
        //return bookingMapper.shortBookingDto(bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()));

        return bookingMapper.shortBookingDto(bookingRepository.findAllByItem_IdAndStartBeforeOrderByStartDesc(itemId, LocalDateTime.now())
                .stream().max(Comparator.comparing(Booking::getStart)).orElse(null));

    }

    @Override
    public Booking getBookedItemWithBooker(Long itemId, Long userId) {
        return bookingRepository.findByItem_IdAndBooker_IdAndEndBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), Status.APPROVED);
    }

    private void checkUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("");
        }
    }

    private boolean isAvailableItem(Long itemId, Long bookerId) {
        return itemService.getItemById(itemId, bookerId).getAvailable();
    }

    private void checkTime(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("");
        }
        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("");
        }
        if (start.equals(end)) {
            throw new ValidationException("");
        }
        if (start.equals(null)) {
            throw new ValidationException("");
        }
        if (end.equals(null)) {
            throw new ValidationException("");
        }
    }
}
