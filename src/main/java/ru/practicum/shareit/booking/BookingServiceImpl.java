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
import ru.practicum.shareit.pageable.Pagination;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new BookingNotFoundException("Бронирование не найдено."));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования истекло. ");
        }
        if (booking.getBooker().getId().equals(bookerId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
            } else {
                throw new UserNotFoundException("Подтверждение должно быть от владельца вещи.");
            }
        } else if (bookerId.equals(booking.getItem().getOwner().getId())
                && !booking.getStatus().equals(Status.CANCELED)) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("Статус уже изменён.");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("Бронирование уже отменено.");
            } else {
                throw new ValidationException("Подтверждение должно быть от владельца вещи.");
            }
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        checkUserId(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new BookingNotFoundException("Бронирование не найдено."));
        if (booking.getBooker().getId().equals(userId) || itemService.getItemsByOwnerId(userId, 0, 20).stream()
                .anyMatch(itemDto -> itemDto.getId().equals(booking.getItem().getId()))) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException(" Проверить данные бронирования может только владелец вещи. ");
        }
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long ownerId, String state, int from, int size) {
        checkUserId(ownerId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_Id(ownerId,
                                Pagination.makePageRequest(from, size, sort))
                        .getContent()
                        .stream()
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                bookings = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(), LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "PAST":
                bookings = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "WAITING":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(ownerId, Status.WAITING, Pagination.makePageRequest(from, size));
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(ownerId, Status.REJECTED, Pagination.makePageRequest(from, size));
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(Long ownerId, String state, int from, int size) {
        checkUserId(ownerId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId, Pagination.makePageRequest(from, size));
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(), LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(ownerId,
                        LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItemOwnerId(ownerId, LocalDateTime.now(), Pagination.makePageRequest(from, size));
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.WAITING, Pagination.makePageRequest(from, size));
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.REJECTED, Pagination.makePageRequest(from, size));
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShortBookingDto getLastBooking(Long itemId) {
        return bookingMapper.toShortBookingDto(bookingRepository.findAllByItemId(itemId).stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart))
                .orElse(null));
    }

    @Override
    public ShortBookingDto getNextBooking(Long itemId) {
        ShortBookingDto lastBookingDto = getLastBooking(itemId);
        LocalDateTime lastBookingEnd;
        if (Objects.isNull(lastBookingDto)) {
            lastBookingEnd = LocalDateTime.now();
        } else {
            lastBookingEnd = lastBookingDto.getEnd();
        }
        return bookingMapper.toShortBookingDto(
                bookingRepository.findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(itemId,
                        Status.APPROVED, lastBookingEnd).orElse(null));
    }

    @Override
    public Booking getBookedItemWithBooker(Long itemId, Long userId) {
        return bookingRepository.findFirstByItem_IdAndBookerIdAndEndIsBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), Status.APPROVED);
    }


    private void checkUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователь не найден ");
        }
    }

    private boolean isAvailableItem(Long itemId, Long bookerId) {
        return itemService.getItemById(itemId, bookerId).getAvailable();
    }

    private void checkTime(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException(" Время не начала бронирования после его окончания. ");
        }
        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException(" Время окончания бронирования в прошедшем времени. ");
        }
        if (start.equals(end)) {
            throw new ValidationException(" Время начала бронирования совпадает с его окончанием. ");
        }
    }
}
