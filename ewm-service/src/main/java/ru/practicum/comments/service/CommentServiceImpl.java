package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto createComment(InputCommentDto inputCommentDto, Long eventId) {
        User author = getUserByIdFromRepository(inputCommentDto.getAuthorId());
        Event event = getEventByIdFromRepository(eventId);
        Comment comment = CommentMapper.inputCommentDtoToCommentDto(inputCommentDto, author, event);
        commentRepository.save(comment);
        log.info("Успешно сохранен комментарий к событию eventId: {}", eventId);
        return CommentMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Event event = getEventByIdFromRepository(eventId);
        log.info("Успешно получены все комментарии к Event с eventId: {}", eventId);
        return commentRepository.findAllCommentsByEvent(event, pageRequest).stream()
                .map(CommentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = getCommentByIdFromRepository(commentId);
        log.info("Успешно получен комментарий по commentId: {}", commentId);
        return CommentMapper.commentToCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long eventId, UpdateCommentDto updateCommentDto) {
        getEventByIdFromRepository(eventId); //проверка что Event существует
        Comment comment = getCommentByIdFromRepository(updateCommentDto.getId());
        comment.setText(updateCommentDto.getText());
        Comment out = commentRepository.save(comment);
        log.info("Успешно обновлен комментарий по commentId: {}", updateCommentDto.getId());
        return CommentMapper.commentToCommentDto(out);
    }

    @Override
    public List<CommentDto> getAllCommentsForAdmin(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        log.info("Успешно получены все комментарии для Admin с параметрами from: {}, size: {}", from, size);
        return commentRepository.findAll(pageRequest).stream()
                .map(CommentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllCommentsByStatusForAdmin(Boolean published, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Comment> out = commentRepository.findByPublished(published, pageRequest);
        log.info("Успешно получены все комментарии для Admin со статусом published: {}", published);
        return out.stream()
                .map(CommentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateCommentStatusForAdmin(Long commentId, Boolean published) {
        Comment comment = getCommentByIdFromRepository(commentId);
        comment.setPublished(published);
        Comment out = commentRepository.save(comment);
        log.info("Успешно обновлен статус у комментария с commentId: {}", commentId);
        return CommentMapper.commentToCommentDto(out);
    }

    @Override
    @Transactional
    public void deleteCommentByIdForAdmin(Long commentId) {
        getCommentByIdFromRepository(commentId); //проверка что комментарий существует
        commentRepository.deleteById(commentId);
        log.info("Успешно удален комментарий с commentId: {}", commentId);
    }

    private Event getEventByIdFromRepository(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }

    private Comment getCommentByIdFromRepository(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }

    private User getUserByIdFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }
}