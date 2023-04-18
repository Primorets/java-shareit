package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository {

    Item saveItem(Item item);

    Item updateItem(Item item);

    Item getItemById(int itemId);

    List<Item> searchItem(String query);

    void deleteItemsByOwner(int ownerId);

    void deleteItemById(int itemId);

    List<Item> getItemsByOwnerId(int ownerId);
}
