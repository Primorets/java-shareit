package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("Пользователь не был зарегестрирован.")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(toList());
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto user, Long id) {
        user.setId(id);
        User user1 = userRepository.findById(user.getId()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не был зарегестрирован."));
        if (user.getEmail() == null) {
            user.setEmail(user1.getEmail());
        }
        if (user.getName() == null) {
            user.setName(user1.getName());
        }
        if (userRepository.findById(id).stream()
                .filter(user2 -> !Objects.equals(user2.getId(), user.getId()))
                .anyMatch(user2 -> user2.getEmail()
                        .equals(user.getEmail()))) {
            throw new DuplicateEmailException("Email уже зарегестрирован");

        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto user) {
        validateUser(user);
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
        } catch (DuplicateEmailException duplicateEmailException) {
            throw new DuplicateEmailException("Email уже зарегестрирован");
        }
    }

    @Override
    public User getUserForBookingMapper(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("Пользователь не был зарегестрирован."));
    }

    private void validateUser(UserDto user) {
        if (!user.getEmail().contains("@") || user.getEmail() == null) {
            throw new ValidationException("Введён не правильный email");
        }
        if (user.getName().isEmpty() || user.getName().contains(" ")) {
            throw new ValidationException("Введено пустое имя");
        }
    }
}
