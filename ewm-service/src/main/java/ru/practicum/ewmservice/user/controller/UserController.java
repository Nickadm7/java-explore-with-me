package ru.practicum.ewmservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;

@Slf4j
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
        log.info("Get запрос к /admin/users с параметрами ids: {}, from: {}, size: {}", ids, from, size);
        return userService.getUser(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    UserDto createUser(@RequestBody InputUserDto inputUserDto) {
        log.info("Post запрос к /admin/users с параметрами inputUserDto: {}", inputUserDto);
        return userService.createUser(inputUserDto);
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") Long userId) {
        log.info("Delete запрос к /admin/users с параметром userId: {}", userId);
        userService.deleteUser(userId);
    }
}