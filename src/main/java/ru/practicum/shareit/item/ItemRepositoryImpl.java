package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private HashMap<Integer,Item> itemsMap;
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
        item.setId(++itemId);
        itemsMap.put(item.getId(),item);
        return itemsMap.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        if(item.getId()==0||!itemsMap.containsKey(item.getId())){
            throw new ValidationException("");
        }
        if(item.getName().isEmpty()||item.getName()==null){
            item.setName(itemsMap.get(item.getId()).getName());
        }
        if (item.getDescription().isEmpty()||item.getDescription()==null){
            item.setDescription(itemsMap.get(item.getId()).getDescription());
        }
        if (item.getAvailable()==null){
            item.setAvailable(itemsMap.get(item.getId()).getAvailable());
        }
        itemsMap.put(item.getId(),item);
        return itemsMap.get(itemId);
    }

    @Override
    public Item getItemById(int itemId) {
        return itemsMap.get(itemId);
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemsMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        ||item.getDescription().toLowerCase().contains(text))
                .collect(toList());
    }

    @Override
    public void deleteItemsByOwner(int ownerId) {
        itemsMap.entrySet().removeIf(integerItemEntry -> integerItemEntry.getValue().getOwner().getId()==ownerId);
    }

    @Override
    public void deleteItemById(int itemId) {
        itemsMap.remove(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(int ownerId) {
        return itemsMap.values().stream().filter(item -> item.getOwner().getId()==ownerId).collect(toList());
    }
}
