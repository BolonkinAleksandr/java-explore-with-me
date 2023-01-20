package ru.practicum.explorewithmemain.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.dto.CommentDto;
import ru.practicum.explorewithmemain.dto.NewCommentDto;
import ru.practicum.explorewithmemain.dto.UpdateCommentDto;
import ru.practicum.explorewithmemain.service.privateservice.PrivateCommentService;
import ru.practicum.explorewithmemain.service.privateservice.PrivateUserEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithmemain.mapper.CommentMapper.toComment;
import static ru.practicum.explorewithmemain.mapper.CommentMapper.toCommentDto;
import static ru.practicum.explorewithmemain.mapper.EventMapper.toEventShortDto;
import static ru.practicum.explorewithmemain.mapper.UserMapper.toUserDto;
import static ru.practicum.explorewithmemain.util.Methods.buildCommentList;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final PrivateCommentService commentService;
    private final PrivateUserEventService privateUserEventService;

    @PostMapping
    public CommentDto create(@RequestBody @Valid NewCommentDto newCommentDto,
                             @PathVariable long userId) {
        log.info("Controller: create comment by userI={}, comment={}", userId, newCommentDto);
        Comment comment = toComment(newCommentDto);
        comment.setCreated(LocalDateTime.now());
        comment.setUser(privateUserEventService.findUserById(userId));
        comment.setEvent(privateUserEventService.findEventById(newCommentDto.getEventId()));
        comment = commentService.addComment(userId, comment);
        CommentDto commentDto = toCommentDto(comment);
        commentDto.setUser(toUserDto(comment.getUser()));
        commentDto.setEvent(toEventShortDto(comment.getEvent()));
        log.info("return creating comment by userI={}, comment={}", userId, commentDto);
        return commentDto;
    }

    @PutMapping
    public CommentDto update(@RequestBody @Valid UpdateCommentDto updateCommentDto,
                             @PathVariable long userId) {
        log.info("update comment by userI={}, comment={}", userId, updateCommentDto);
        Comment comment = toComment(updateCommentDto);
        comment = commentService.update(userId, comment);
        CommentDto commentDto = toCommentDto(comment);
        commentDto.setUser(toUserDto(comment.getUser()));
        commentDto.setEvent(toEventShortDto(comment.getEvent()));
        log.info("Controller: return updating comment by userI={}, comment={}", userId, commentDto);
        return commentDto;
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        log.info("delete comment by userI={}, commentId={}", userId, commentId);
        commentService.delete(userId, commentId);
    }


    @GetMapping
    public List<CommentDto> findCommentsByText(@PathVariable long userId,
                                               @RequestParam(name = "text", defaultValue = "") String text,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        List<Comment> comments = commentService.findCommentsByText(userId, text, from, size);
        List<CommentDto> commentDtos = buildCommentList(comments);
        log.info("Controller: find comments by text={}: {}", text, commentDtos);
        return commentDtos;
    }
}
