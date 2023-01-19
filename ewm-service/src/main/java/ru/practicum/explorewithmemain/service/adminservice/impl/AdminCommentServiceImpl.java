package ru.practicum.explorewithmemain.service.adminservice.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.exceptions.ForbiddenException;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.repository.CommentRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.service.adminservice.AdminCommentService;

import java.util.List;

@Service
@Slf4j
public class AdminCommentServiceImpl implements AdminCommentService {

    private CommentRepository commentRepository;
    private EventRepository eventRepository;

    @Autowired
    public AdminCommentServiceImpl(CommentRepository commentRepository,
                                   EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public Comment approve(long commentId) {
        Comment comment = findAndValidateComment(commentId);
        if (comment.getStatus() == State.APPROVE) {
            throw new ForbiddenException("comment can't be approved twice", String.format("comment: {}", comment));
        }
        comment.setStatus(State.APPROVE);
        log.info("approve comment by commentId={}: {}", commentId, comment);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment reject(long commentId) {
        Comment comment = findAndValidateComment(commentId);
        if (comment.getStatus() == State.CANCELED) {
            throw new ForbiddenException("comment can't be reject twice", String.format("comment: {}", comment));
        }
        comment.setStatus(State.CANCELED);
        log.info("reject comment by commentId={}: {}", commentId, comment);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findComments(long eventId, State status, int from, int size) {
        findAndValidateEvent(eventId);
        final Pageable pageable = CustomPageable.of(from, size);
        List<Comment> comments = commentRepository.findByEventIdAndStatus(eventId, status, pageable);
        log.info("find admin comments by eventId={} and status={}: {}", eventId, status, comments);
        return comments;
    }

    @Override
    @Transactional
    public void delete(long commentId) {
        findAndValidateComment(commentId);
        log.info("delete comment by commentId={}", commentId);
        commentRepository.deleteById(commentId);
    }

    private Comment findAndValidateComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Comment with commentId = {} was not found", commentId));
        }
        return commentRepository.getReferenceById(commentId);
    }

    private Event findAndValidateEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Event with eventId = {} was not found", eventId));
        }
        return eventRepository.getReferenceById(eventId);
    }
}
