package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public List<CommentDto> getAllCommentsForAdmin(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get запрос к /admin/comments с параметрами from: {}, size: {}", from, size);
        return commentService.getAllCommentsForAdmin(from, size);
    }

    @GetMapping("/comments/published")
    public List<CommentDto> getAllCommentsByStatusForAdmin(
            @RequestParam(defaultValue = "false") Boolean published,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get запрос к /admin/comments/published с параметрами published: {}, from: {}, size: {}",
                published, from, size);
        return commentService.getAllCommentsByStatusForAdmin(published, from, size);
    }

    @PatchMapping("/comments/published")
    public CommentDto updateCommentStatusForAdmin(
            @RequestParam @PositiveOrZero Long commentId,
            @RequestParam Boolean published) {
        log.info("Patch запрос к /admin/comments/published смена статуса с параметрами commentId: {}", commentId);
        return commentService.updateCommentStatusForAdmin(commentId, published);
    }

    @DeleteMapping(path = "/comments/{commentId}")
    public void deleteCommentByIdForAdmin(
            @PathVariable Long commentId) {
        log.info("Delete запрос к /admin/comments/published commentId: {}", commentId);
        commentService.deleteCommentByIdForAdmin(commentId);
    }
}