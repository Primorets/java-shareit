package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.Generated;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotBlank(groups = {Create.class})
    private Boolean available;
    @NotBlank(groups = {Create.class})
    private User owner;
    @NotBlank(groups = {Create.class})
    private String request;
}
