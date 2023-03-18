package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping(path = "/{eventId}/comment")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createComment(@Validated @RequestBody InputCommentDto inputCommentDto,
                                    @PathVariable Long eventId) {
        log.info("Post запрос к /event/{eventId}/comment с параметрами text: {}, authorId: {}, eventId: {}",
                inputCommentDto.getText(), inputCommentDto.getAuthorId(), eventId);
        return commentService.createComment(inputCommentDto, eventId);
    }

    @GetMapping(path = "/{eventId}/comment")
    public List<CommentDto> getAllCommentsByEventId(@Valid @PathVariable @PositiveOrZero Long eventId,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("Get запрос к /event/{eventId}/comment с параметрами eventId: {}", eventId);
        return commentService.getAllCommentsByEventId(eventId, from, size);
    }

    @GetMapping(path = "/{eventId}/comment/{commentId}")
    public CommentDto getCommentById(@Valid @PathVariable @PositiveOrZero Long eventId,
                                     @Valid @PathVariable @PositiveOrZero Long commentId) {
        log.info("Get запрос к /event//{eventId}/comment/{commentId} с параметрами eventId: {}, commentId: {}",
                eventId, commentId);
        return commentService.getCommentById(commentId);
    }

    @PatchMapping("/{eventId}/comment")
    public CommentDto updateComment(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("Patch запрос к /event/{eventId}/comment с параметрами eventId: {}", eventId);
        return commentService.updateComment(eventId, updateCommentDto);
    }
}