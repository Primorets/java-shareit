package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.*;


@SpringBootApplication
@ComponentScan(basePackageClasses = {UserRepository.class, ItemRepository.class, BookingRepository.class, UserController.class, UserService.class, ItemService.class, UserMapper.class, ItemMapper.class, BookingMapper.class, UserServiceImpl.class, UserRepository.class, ItemServiceImpl.class, ItemController.class, ItemRepository.class, BookingRepository.class, BookingController.class, BookingServiceImpl.class})
//@EntityScan(basePackageClasses = {User.class, Item.class, Comment.class, Booking.class})
public class ShareItApp {

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
