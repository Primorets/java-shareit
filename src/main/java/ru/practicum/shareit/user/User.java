package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {

    private int id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Email
    @NotBlank(groups = {Create.class})
    private String email;
}
