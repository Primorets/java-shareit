package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public interface ItemRepository {
        User findUserById(int id);

        Item saveItem(Item item);

        Item updateItem(Item item);
}
