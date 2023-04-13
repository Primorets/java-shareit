package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

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
    public ItemDto getItem (@PathVariable(value = "id") int id){
        log.info("Получен запрос на получение пользователя по ID: " + id);
        return itemService.getItemById(id);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(OWNER) int userId,
                              @Validated({Create.class}) @RequestBody ItemDto itemDto){
        log.info("");
        return itemService.createItem(itemDto);
    }

    @PatchMapping
    public ItemDto updateItem(@RequestHeader(OWNER) int userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto){
        return itemService.updateItem(userId, itemDto);
    }



    @GetMapping("/search")
    public ItemDto searchItem(@RequestParam String query){
        log.info("Получен запрос на получение пользователя");
        return itemService.searchItem();
    }

}
