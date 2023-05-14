package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable(value = "requestId") Long itemRequestId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос на получение предмета по ID: " + userId);
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllOwnerItemRequests(@RequestHeader(USER_ID) Long ownerId) {
        log.info("Получен запрос на получение всех запросов на предмет.");
        return itemRequestService.getAllOwnerItemRequests(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("Получен запрос на получение всех запросов");
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader(USER_ID) Long requestorId) {
        log.info("Добавлен запрос : " + requestorId + " пользователем c ID: ");
        return itemRequestService.createItemRequest(itemRequestDto, requestorId, LocalDateTime.now());
    }
}
