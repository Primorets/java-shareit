package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String OWNER = "X-Sharer-User-Id";

    @Autowired
    ItemServiceImpl itemService;

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable(value = "id") int id) {
        log.info("Получен запрос на получение пользователя по ID: " + id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemsForOwnerById(@RequestHeader(OWNER) int ownerId) {
        return itemService.getItemsByOwnerId(ownerId);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@RequestHeader(OWNER) int ownerId,
                              @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("");
        return itemService.createItem(itemDto, ownerId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(OWNER) int ownerId, @PathVariable int itemId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemService.updateItem(ownerId, itemDto, itemId);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable(value = "id") int id) {
        itemService.deleteItemById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String query) {
        log.info("Получен запрос на получение пользователя");
        return itemService.searchItem(query);
    }

}
