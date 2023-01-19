package ru.practicum.explorewithmemain.service.publicservice;

import ru.practicum.explorewithmemain.model.Comment;

import java.util.List;

public interface PublicCommentService {
    List<Comment> findCommentsByUser(long userId, int from, int size);
    List<Comment> findCommentsByEvent(long eventId, int from, int size);
    Comment findCommentById(long commentId);
}
