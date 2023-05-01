package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(Long ownerId, ItemDto itemDto, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> searchItem(String query);

    void deleteItemById(Long itemId);

    List<ItemDto> getItemsByOwnerId(Long ownerId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    List<CommentDto> getCommentsToIemByItemId(Long itemId);
}
