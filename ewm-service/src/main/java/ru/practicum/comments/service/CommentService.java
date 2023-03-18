package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(InputCommentDto inputCommentDto, Long eventId);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    CommentDto getCommentById(Long commentId);

    CommentDto updateComment(Long eventId, UpdateCommentDto updateCommentDto);

    List<CommentDto> getAllCommentsForAdmin(int from, int size);

    List<CommentDto> getAllCommentsByStatusForAdmin(Boolean published, int from, int size);

    CommentDto updateCommentStatusForAdmin(Long commentId, Boolean published);

    void deleteCommentByIdForAdmin(Long commentId);
}