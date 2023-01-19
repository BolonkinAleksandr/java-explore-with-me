package ru.practicum.explorewithmemain.service.publicservice.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.model.User;
import ru.practicum.explorewithmemain.repository.CommentRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.repository.UserRepository;
import ru.practicum.explorewithmemain.service.publicservice.PublicCommentService;

import java.util.List;

@Service
@Slf4j
public class PublicCommentServiceImpl implements PublicCommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;

    @Autowired
    public PublicCommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    @Override
    public List<Comment> findCommentsByUser(long userId, int from, int size) {
        findAndValidateUser(userId);
        final Pageable pageable = CustomPageable.of(from, size);
        List<Comment> comments = commentRepository.findByUserId(userId, pageable);
        log.info("find comments by userId={}: {}", userId, comments);
        return comments;
    }

    @Override
    public List<Comment> findCommentsByEvent(long eventId, int from, int size) {
        findAndValidateEvent(eventId);
        final Pageable pageable = CustomPageable.of(from, size);
        List<Comment> comments = commentRepository.findByEventId(eventId, pageable);
        log.info("find comments by eventId={}: {}", eventId, comments);
        return comments;
    }

    @Override
    public Comment findCommentById(long commentId) {
        Comment comment = findAndValidateComment(commentId);
        log.info("find comments by commentId={}: {}", commentId, comment);
        return comment;
    }

    private User findAndValidateUser(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("object doesn't found ",
                    String.format("User with userId = {} was not found", userId));
        }
        return userRepository.getReferenceById(userId);
    }

    private Event findAndValidateEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Event with eventId = {} was not found", eventId));
        }
        return eventRepository.getReferenceById(eventId);
    }

    private Comment findAndValidateComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Comment with commentId = {} was not found", commentId));
        }
        return commentRepository.getReferenceById(commentId);
    }
}
