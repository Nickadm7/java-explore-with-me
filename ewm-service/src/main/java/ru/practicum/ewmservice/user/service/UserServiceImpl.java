package ru.practicum.ewmservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.ewmservice.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUser(List<Long> ids, int from, int size) {
        List<User> outUser;
        if (ids.isEmpty()) {
            outUser = userRepository.findAll(PageRequest.of(from / size, size)).toList();
        } else {
            outUser = userRepository.findAllById(ids);
        }
        log.info("Получены User c id: {}", ids);
        return outUser.stream()
                .map(UserMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(InputUserDto inputUserDto) {
        if (inputUserDto == null) {
            throw new ValidationException("inputUserDto не должен быть null");
        }
        if (inputUserDto.getName() == null || inputUserDto.getName().isEmpty()) {
            throw new ValidationException("inputUserDto Name не должен быть null");
        }
        if (inputUserDto.getEmail() == null || inputUserDto.getEmail().isEmpty()) {
            throw new ValidationException("inputUserDto Email не должен быть null");
        }
        User user = UserMapper.inputUserDtoToUser(inputUserDto);
        userRepository.save(user);
        log.info("Сохранен User с параметрами id: {}, name: {}, email: {}", user.getId(), user.getName(), user.getEmail());
        return UserMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userId <= 0) {
            throw new ValidationException("userId должен быть положительным");
        }
        userRepository.deleteById(userId);
        log.info("Удален user с id: {}", userId);
    }
}