package ru.practicum.ewmservice.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> get(@RequestParam(name = "ids", defaultValue = "") List<Long> ids,
                             @RequestParam(name = "from", defaultValue = "0") int from,
                             @RequestParam(name = "size", defaultValue = "10") int size) {
        return userService.getUser(ids, from, size);
    }

    @PostMapping
    UserDto createUser(@RequestBody InputUserDto inputUserDto) {
        return userService.createUser(inputUserDto);
    }

    @DeleteMapping(path = "/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

}
