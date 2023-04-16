package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, int ownerId){
        validationItem(itemDto);
        User owner = itemRepository.findUserById(itemDto.getOwner().getId());
        Item item = ItemMapper.toItem(itemDto,owner);
        return ItemMapper.toItemDto(itemRepository.saveItem(item));
    }
    @Override
    public ItemDto updateItem(int ownerId, ItemDto itemDto, int itemId){
        itemDto.setId(itemId);
        User owner = itemRepository.findUserById(ownerId);
        Item item = ItemMapper.toItem(itemDto,owner);
        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }
    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        text = text.toLowerCase();
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public void deleteItemById(int itemId){
        itemRepository.deleteItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(int ownerId) {
        return itemRepository.getItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    private void validationItem (ItemDto itemDto){
        if(itemDto.getName().isEmpty()|| itemDto.getDescription().isEmpty()||itemDto.getAvailable()==null){
            throw new ValidationException("");
        }
    }
}
