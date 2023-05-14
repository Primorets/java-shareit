package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

@Component
public class ItemRequestMapper {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, LocalDateTime createdTime, Long requestorId) {
        return new ItemRequest(
                null,
                itemRequestDto.getDescription(),
                UserMapper.toUser(userService.getUserById(requestorId)),
                createdTime
        );
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                itemService.getItemsByRequestId(itemRequest.getId()));
    }
}
