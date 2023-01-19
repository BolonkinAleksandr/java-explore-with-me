package ru.practicum.explorewithmemain.service.privateservice.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.exceptions.ForbiddenException;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.model.User;
import ru.practicum.explorewithmemain.repository.CommentRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.UserRepository;
import ru.practicum.explorewithmemain.service.privateservice.PrivateCommentService;

import java.util.List;

@Service
@Slf4j
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public PrivateCommentServiceImpl(CommentRepository commentRepository,
                                     UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Comment addComment(long userId, Comment comment) {
        findAndValidateUser(userId);
        log.info("add comment by userId={}", userId);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long userId, Comment comment) {
        Comment newComment = findAndValidateComment(comment.getId());
        findAndValidateUser(userId);
        checkUsersComment(userId, comment.getId());
        newComment.setText(comment.getText());
        newComment.setStatus(State.PENDING);
        log.info("update comment by userId={}: {}", userId, newComment);
        return commentRepository.save(newComment);
    }

    @Override
    @Transactional
    public void delete(long userId, long commentId) {
        findAndValidateUser(userId);
        findAndValidateComment(commentId);
        checkUsersComment(userId, commentId);
        log.info("delete comment by userId={}, commentId={}", userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> findCommentsByText(long userId, String text, int from, int size) {
        findAndValidateUser(userId);
        final Pageable pageable = CustomPageable.of(from, size);
        List<Comment> comments = commentRepository.findCommentsByTextContainingIgnoreCase(text, pageable);
        log.info("find comments by text={}: {}", text, comments);
        return comments;
    }

    private User findAndValidateUser(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("object doesn't found ",
                    String.format("User with userId = {} was not found", userId));
        }
        return userRepository.getReferenceById(userId);
    }

    private Comment findAndValidateComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Comment with commentId = {} was not found", commentId));
        }
        return commentRepository.getReferenceById(commentId);
    }

    private void checkUsersComment(long userId, long commentId) {
        if (commentRepository.getReferenceById(commentId).getUser() != userRepository.getReferenceById(userId)) {
            throw new ForbiddenException("user isn't comment author",
                    String.format("User with userId = {} is not author comment with commentIs = {}", userId, commentId));
        }
    }
}
