package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdOrderByStartDesc(Long id);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long id, Status status);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long id, Status status);

    List<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);

    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long id);

    @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.start > :currentTime ORDER BY b.start DESC ")
    List<Booking> findByItemOwnerId(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);

    List<Booking> findAllByItemId(Long itemId);

    Booking findFirstByItem_IdAndBookerIdAndEndIsBeforeAndStatus(Long itemId, Long userId, LocalDateTime now, Status status);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime end);

    Optional<Booking> findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime now);

}
