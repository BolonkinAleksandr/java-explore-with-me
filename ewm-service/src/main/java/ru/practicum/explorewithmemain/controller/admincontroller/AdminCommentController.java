package ru.practicum.explorewithmemain.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.service.adminservice.AdminCommentService;
import ru.practicum.explorewithmemain.dto.CommentDto;
import ru.practicum.explorewithmemain.model.Comment;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

import static ru.practicum.explorewithmemain.mapper.CommentMapper.toCommentDto;
import static ru.practicum.explorewithmemain.mapper.EventMapper.toEventShortDto;
import static ru.practicum.explorewithmemain.mapper.UserMapper.toUserDto;
import static ru.practicum.explorewithmemain.util.Methods.buildCommentList;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Validated
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @PatchMapping("/{commentId}/approve")
    public CommentDto approve(@PathVariable long commentId) {
        log.info("approve comment by commentId={}", commentId);
        Comment comment = adminCommentService.approve(commentId);
        CommentDto commentDto = toCommentDto(comment);
        commentDto.setUser(toUserDto(comment.getUser()));
        commentDto.setEvent(toEventShortDto(comment.getEvent()));
        return commentDto;
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto reject(@PathVariable long commentId) {
        log.info("reject comment by commentId={}", commentId);
        Comment comment = adminCommentService.reject(commentId);
        CommentDto commentDto = toCommentDto(comment);
        commentDto.setUser(toUserDto(comment.getUser()));
        commentDto.setEvent(toEventShortDto(comment.getEvent()));
        return commentDto;
    }

    @DeleteMapping("/{commentId}")
    public void adminDelete(@PathVariable long commentId) {
        log.info("delete from admin comment by commentId={}", commentId);
        adminCommentService.delete(commentId);
    }

    @GetMapping
    public List<CommentDto> adminFindByEventAndStatus(@RequestParam State status,
                                                      @RequestParam long eventId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Controller: find admin comments by eventId={} и status={}}", eventId, status);
        List<Comment> comments = adminCommentService.findComments(eventId, status, from, size);
        List<CommentDto> commentDtos = buildCommentList(comments);
        log.info("Controller: return admin comments by eventId={} и status={}: {}}", eventId, status, commentDtos);
        return commentDtos;
    }
}
