package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByBooker_IdAndStatus(Long id, Status status,Sort sort);
    List<Booking> findByBooker_IdAndStartAfter(Long id, LocalDateTime start,Sort sort);
    List<Booking> findByBooker_IdAndEndBefore(Long id, LocalDateTime end,Sort sort);
    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(Long id, LocalDateTime start, LocalDateTime end,Sort sort);
    List<Booking> findByBooker_Id(Long id,Sort sort);
    Booking findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long id, LocalDateTime end);
    Booking findFirstByItem_IdAndStartAfterOrderByStartAsc(Long id, LocalDateTime start);
    List<Booking> findByItem_Owner_IdAndStatus(Long id,Status status, Sort sort);
    List<Booking> findByItem_Owner_IdAndStartAfter(Long id, LocalDateTime start, Sort sort);
    List<Booking> findByItem_Owner_IdAndEndBefore(Long id, LocalDateTime end,Sort sort);
    List<Booking> findByItem_IdAndStartBeforeAndEndAfter(Long id, LocalDateTime start, LocalDateTime end, Sort sort);
    List<Booking> findByItem_Owner_Id(Long id, Sort sort);
    Booking findByItem_IdAndBooker_IdAndEndBeforeAndStatus(Long id, Long id1, LocalDateTime end, Status status);
    Booking getBookingById(Long bookingId);


}
