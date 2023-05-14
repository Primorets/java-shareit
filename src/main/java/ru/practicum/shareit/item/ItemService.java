package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(Long ownerId, ItemDto itemDto, Long itemId);

    ItemDto getItemById(Long itemId, Long userId);

    Item getItemByIdForBookingMapper(Long id);

    List<ItemDto> searchItem(String query, int from, int size);

    void deleteItemById(Long itemId);

    List<ItemDto> getItemsByOwnerId(Long ownerId, int from, int size);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    List<CommentDto> getCommentsToIemByItemId(Long itemId);

    List<ItemDto> getItemsByRequestId(Long id);
}
