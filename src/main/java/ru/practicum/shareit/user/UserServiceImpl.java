package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(UserMapper::toUserDto).collect(toList());
    }

    @Override
    public void deleteUserById(int id) {
        itemRepository.deleteItemsByOwner(id);
        userRepository.deleteUserById(id);
    }

    @Override
    public UserDto updateUser(User user, int id) {
        return UserMapper.toUserDto(userRepository.updateUser(user, id));
    }

    @Override
    public UserDto createUser(User user) {
        validateUser(user);
        return UserMapper.toUserDto(userRepository.saveUser(user));
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@") || user.getEmail() == null) {
            throw new ValidationException("Введён не правильный email");
        }
        if (user.getName().isEmpty() || user.getName().contains(" ")) {
            throw new ValidationException("Введено пустое имя");
        }
    }
}
