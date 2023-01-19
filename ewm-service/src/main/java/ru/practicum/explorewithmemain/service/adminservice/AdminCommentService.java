package ru.practicum.explorewithmemain.service.adminservice;

import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.State;

import java.util.List;

public interface AdminCommentService {

    Comment approve(long commentId);

    Comment reject(long commentId);

    List<Comment> findComments(long eventId, State status, int from, int size);

    void delete(long commentId);
}
