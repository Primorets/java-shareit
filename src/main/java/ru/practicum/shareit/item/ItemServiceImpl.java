package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;

@Service
public class ItemServiceImpl implements ItemService{


    @Autowired
    ItemRepository itemRepository;

    public ItemDto createItem(ItemDto itemDto, ItemMapper){
        User owner = itemRepository.findUsrById;
        Item item = ItemMapper.toItemDto(itemDto,owner);
        return item;
    }

    public ItemDto updateItem(){

    }

    public ItemDto getItemById() {

    }

    @Override
    public ItemDto searchItem() {
        return null;
    }
}
