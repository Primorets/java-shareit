package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Email
    @NotBlank(groups = {Create.class})
    private String email;
}
