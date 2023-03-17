package ru.practicum.user.service;

import ru.practicum.user.dto.InputUserDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUser(List<Long> ids, int from, int size);

    UserDto createUser(InputUserDto inputUserDto);

    void deleteUser(Long userId);
}
