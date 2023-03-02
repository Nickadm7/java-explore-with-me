package ru.practicum.ewmservice.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.dto.UserShortDto;
import ru.practicum.ewmservice.user.model.User;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User inputUserDtoToUser(InputUserDto inputUserDto) {
        User user = new User();
        user.setName(inputUserDto.getName());
        user.setEmail(inputUserDto.getEmail());
        return user;
    }

    public static UserDto userToUserDto (User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto userToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
