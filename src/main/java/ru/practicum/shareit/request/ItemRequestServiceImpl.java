package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.pageable.Pagination;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestMapper itemRequestMapper;

    private final UserService userService;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRequestMapper itemRequestMapper, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestMapper = itemRequestMapper;
        this.userService = userService;
    }

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requestorId, LocalDateTime now) {
        validRequest(itemRequestDto);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequestMapper.toItemRequest(itemRequestDto, now, requestorId)));
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        checkUserId(userId);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new ItemRequestNotFoundException("")));
    }

    @Override
    public List<ItemRequestDto> getAllOwnerItemRequests(Long requestorId) {
        checkUserId(requestorId);
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(toList());
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size)
            throws ItemRequestNotFoundException {
        checkUserId(userId);
        if (size < 1 || from < 0) {
            throw new ValidationException(" ");
        }
        return itemRequestRepository.findAll(Pagination.makePageRequest(from, size)).stream()
                .collect(toList())
                .stream()
                .filter(itemRequest -> !itemRequest.getRequestor().getId().equals(userId))
                .map(itemRequestMapper::toItemRequestDto)
                .collect(toList());
    }

    private void checkUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователь не найден ");
        }
    }

    private void validRequest(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null) {
            throw new ValidationException("Отсутствует описание запроса. ");
        }
    }
}
