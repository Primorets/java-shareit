package ru.practicum.shareit.item.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {

    @Autowired
    private BookingService bookingService;

    @Autowired
    @Lazy
    private ItemService itemService;

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId()!=null? item.getRequestId() : null,
                null,
                null,
                itemService.getCommentsToIemByItemId(item.getId()));
    }

    public  Item toItem(ItemDto itemDto, User user) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                itemDto.getRequestId()!=null? itemDto.getRequestId() : null);
    }

    public CommentDto toCommentDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public ItemDto toFullItemDto(Item item){
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId()!=null? item.getRequestId() : null,
                bookingService.getLastBooking(item.getId()),
                bookingService.getNextBooking(item.getId()),
                itemService.getCommentsToIemByItemId(item.getId())
                );
    }
}
