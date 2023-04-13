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

    public ItemDto createItem(ItemDto itemDto){
        User owner = itemRepository.findUserById(itemDto.getOwner().getId());
        Item item = ItemMapper.toItem(itemDto,owner);
        return ItemMapper.toItemDto(itemRepository.saveItem(item),owner);
    }

    public ItemDto updateItem(int userId, ItemDto itemDto){
        User owner = itemRepository.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto,owner);
        return ItemMapper.toItemDto(itemRepository.updateItem(item),owner);
    }

    public ItemDto getItemById(int itemId) {


        return null;
    }

    @Override
    public ItemDto searchItem() {
        return null;
    }

    private void validationItem (ItemDto ){

    }
}
