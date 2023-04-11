package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    @NotBlank(groups = {Create.class})
    private LocalDateTime start;
    @NotBlank(groups = {Create.class})
    private LocalDateTime end;
    @NotBlank(groups = {Create.class})
    private Item item;
    @NotBlank(groups = {Create.class})
    private User booker;
    @NotBlank(groups = {Create.class})
    private Status status;
}
