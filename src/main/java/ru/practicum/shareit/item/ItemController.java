package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemServiceImpl itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId, @Validated({Create.class}) @RequestBody ItemDto itemDto){
        return itemService.createItem(itemDto);
    }

    @PatchMapping
    public ItemDto updateItem(@Validated({Update.class}) @RequestBody ItemDto itemDto){
        return itemService.updateItem();
    }

    @GetMapping("/{id}")
    public ItemDto getItem (@PathVariable(value = "id") int id){
        return itemService.getItemById();
    }

    @GetMapping("/search")
    public ItemDto searchItem(@RequestParam String query){
        return itemService.searchItem();
    }

}
