package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBooker_IdOrderByStartDesc(Long id);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long id, Status status);
    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);
    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);
    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);
    List<Booking> findByBooker_Id(Long id);
    Booking findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long id, LocalDateTime end);
    Booking findFirstByItem_IdAndStartAfterOrderByStartAsc(Long id, LocalDateTime start);
    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long id,Status status);
    List<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);
    List<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);
    List<Booking> findByItem_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);
    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long id);
    Booking findByItem_IdAndBooker_IdAndEndBeforeAndStatus(Long id, Long id1, LocalDateTime end, Status status);
    Booking getBookingById(Long bookingId);

    Booking getFirstByItem_IdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime localDateTime);

   List <Booking> findAllByItem_IdAndStartBeforeOrderByStartDesc(Long id,LocalDateTime localDateTime);

   List <Booking> findAllByItem_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime localDateTime);
   @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.start > :currentTime ORDER BY b.start DESC ")
    List<Booking> findByItemOwnerId(@Param("userId") Long userId,@Param("currentTime")LocalDateTime currentTime);


}
