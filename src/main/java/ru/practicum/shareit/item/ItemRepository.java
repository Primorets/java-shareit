package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getItemsByOwnerId(Long ownerId);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%'))" +
            " OR LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))" +
            " AND i.available=true")
    List<Item> searchItem(@Param("search") String query);
}
