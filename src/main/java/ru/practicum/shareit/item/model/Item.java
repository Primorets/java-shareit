package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private int id;
    @NotBlank(groups = {Create.class})
    private String name;
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private String request;
}
