package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
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
    public ItemDto getItem(@PathVariable(value = "id") Long id) {
        log.info("Получен запрос на получение предмета по ID: " + id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemsForOwnerById(@RequestHeader(OWNER) Long ownerId) {
        log.info("Получен запрос на получение всех предметов.");
        return itemService.getItemsByOwnerId(ownerId);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                              @RequestHeader(OWNER) Long ownerId) {
        log.info("Добавлен предмет : " + itemDto + " пользователем c ID: " + ownerId);
        return itemService.createItem(itemDto, ownerId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(OWNER) Long ownerId, @PathVariable Long itemId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на изменение данных предмета с ID: " + itemDto);
        return itemService.updateItem(ownerId, itemDto, itemId);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable(value = "id") Long id) {
        log.info("Получен запрос на удаление предмета с ID: " + id);
        itemService.deleteItemById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Получен запрос на получение предмета по строке");
        return itemService.searchItem(text);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader(OWNER) Long userId,
                                    @PathVariable Long itemId){
        return itemService.createComment(commentDto,itemId,userId);
    }
}
