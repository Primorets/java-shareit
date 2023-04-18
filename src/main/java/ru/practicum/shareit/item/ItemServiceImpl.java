package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, int ownerId) {
        validationItem(itemDto);
        User owner = userRepository.getUserById(ownerId);
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.saveItem(item));
    }

    @Override
    public ItemDto updateItem(int ownerId, ItemDto itemDto, int itemId) {
        if (itemRepository.getItemById(itemId).getOwner().getId() != ownerId) {
            throw new UserNotFoundException("Предмет добавлен другим пользователем.");
        }
        User owner = userRepository.getUserById(ownerId);
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public void deleteItemById(int itemId) {
        itemRepository.deleteItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(int ownerId) {
        return itemRepository.getItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    private void validationItem(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
            throw new ValidationException("Ошибка в теле запроса. ");
        }
    }
}
