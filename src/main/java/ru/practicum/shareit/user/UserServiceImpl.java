package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(()->new UserNotFoundException("")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(toList());
    }

    @Override
    public void deleteUserById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public UserDto updateUser(User user, Long id) {
        user.setId(id);
        User user1 = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException(""));
        if (userRepository.findById(id).stream()
                .filter(user2 -> !Objects.equals(user2.getId(), user.getId()))
                .anyMatch(user2 -> user2.getEmail()
                        .equals(user.getEmail()))) {
            throw new DuplicateEmailException("Email уже зарегестрирован");

        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto createUser(User user) {
        validateUser(user);
        try {
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (DuplicateEmailException duplicateEmailException) {
        throw new DuplicateEmailException("Email уже зарегестрирован");
        }

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
