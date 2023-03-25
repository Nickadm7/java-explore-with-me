package ru.practicum.comments.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment inputCommentDtoToCommentDto(InputCommentDto inputCommentDto, User author, Event event) {
        return Comment.builder()
                .text(inputCommentDto.getText())
                .created(LocalDateTime.now())
                .author(author)
                .event(event)
                .published(false)
                .build();
    }

    public static CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .published(comment.getPublished())
                .build();
    }
}