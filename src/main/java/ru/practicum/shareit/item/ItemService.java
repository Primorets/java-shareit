package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, int ownerId);

    ItemDto updateItem(int ownerId, ItemDto itemDto, int itemId);

    ItemDto getItemById(int itemId);

    List<ItemDto> searchItem(String query);

    void deleteItemById(int itemId);

    List<ItemDto> getItemsByOwnerId(int ownerId);
}
