package ru.practicum.explorewithmemain.service.privateservice;

import ru.practicum.explorewithmemain.model.Comment;

import java.util.List;

public interface PrivateCommentService {
    Comment addComment(long userId, Comment comment);
    Comment update(long userId, Comment updateComment);
    void delete(long userId, long commentId);
    List<Comment> findCommentsByText(long userId, String text, int from, int size);
}
