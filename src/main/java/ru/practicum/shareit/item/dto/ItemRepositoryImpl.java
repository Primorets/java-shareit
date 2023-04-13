package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;

@Component
public class ItemRepositoryImpl implements ItemRepository{

    private HashMap<Integer,ItemDto> itemsMap;
    private Integer itemId;

    public ItemRepositoryImpl() {
        itemId = 0;
        itemsMap = new HashMap<>();
    }

    @Override
    public User findUserById(int id) {
        return null;
    }

    @Override
    public Item saveItem(Item item) {
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }
}
