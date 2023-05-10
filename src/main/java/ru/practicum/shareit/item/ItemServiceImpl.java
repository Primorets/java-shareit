package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private BookingService bookingService;

    @Autowired
    @Lazy
    private CommentRepository commentRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        validationItem(itemDto);
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(""));
        Item item = itemMapper.toItem(itemDto, owner);
        itemMapper.toItemDto(item);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long ownerId, ItemDto itemDto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с ID: " + itemId + "отсутствует."));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new UserNotFoundException("Предмет добавлен другим пользователем.");
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        ItemDto itemDto;
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Предмет не найден."));
        if (userId.equals(item.getOwner().getId())) {
            itemDto = itemMapper.toFullItemDto(item);
        } else {
            itemDto = itemMapper.toItemDto(item);
        }
        return itemDto;
    }

    @Override
    public Item getItemByIdForBookingMapper(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с ID: " + itemId + "отсутствует."));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        return itemRepository.searchItem(text).stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public void deleteItemById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        return itemRepository.getItemsByOwnerId(ownerId).stream()
                .map(itemMapper::toFullItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        Comment comment = new Comment();
        Booking booking = bookingService.getBookedItemWithBooker(itemId, userId);
        if (booking != null) {
            comment.setCreated(LocalDateTime.now());
            comment.setItem(booking.getItem());
            comment.setAuthor(booking.getBooker());
            comment.setText(commentDto.getText());
        } else {
            throw new ValidationException("Пользователь не бронировал вещь.");
        }
        return itemMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsToIemByItemId(Long itemId) {
        return commentRepository.findAllByItem_IdOrderByCreatedDesc(itemId).stream()
                .map(itemMapper::toCommentDto)
                .collect(toList());
    }

    private void validationItem(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ValidationException("Ошибка в теле запроса. Отсутствует имя. ");
        } else if (itemDto.getDescription() == null) {
            throw new ValidationException("Ошибка в теле запроса. Отсутствует описание. ");
        } else if (itemDto.getAvailable() == null) {
            throw new ValidationException("Ошибка в теле запроса. Отсутствует статус.");
        }
    }
}
