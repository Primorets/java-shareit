package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requestorId, LocalDateTime now);

    ItemRequestDto getItemRequestById(Long itemRequestId, Long userId);

    List<ItemRequestDto> getAllOwnerItemRequests(Long ownerId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
