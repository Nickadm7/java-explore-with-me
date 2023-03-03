package ru.practicum.ewmservice.user.service;

import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUser(List<Long> ids, int from, int size);

    UserDto createUser(InputUserDto inputUserDto);

    void deleteUser(Long userId);
}
