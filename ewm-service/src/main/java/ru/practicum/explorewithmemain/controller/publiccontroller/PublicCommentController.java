package ru.practicum.explorewithmemain.controller.publiccontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.dto.CommentDto;
import ru.practicum.explorewithmemain.service.publicservice.PublicCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithmemain.mapper.CommentMapper.toCommentDto;
import static ru.practicum.explorewithmemain.mapper.EventMapper.toEventShortDto;
import static ru.practicum.explorewithmemain.mapper.UserMapper.toUserDto;
import static ru.practicum.explorewithmemain.util.Methods.buildCommentList;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class PublicCommentController {

    private final PublicCommentService publicCommentService;


    @GetMapping("/user/{userId}")
    public List<CommentDto> findCommentsByUser(@PathVariable long userId,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        List<Comment> comments = publicCommentService.findCommentsByUser(userId, from, size);
        List<CommentDto> commentDtos = buildCommentList(comments);
        log.info("find comments by userId={}", userId);
        return commentDtos;
    }

    @GetMapping("/event/{eventId}")
    public List<CommentDto> findCommentsByEvent(@PathVariable long eventId,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        List<Comment> comments = publicCommentService.findCommentsByEvent(eventId, from, size);
        List<CommentDto> commentDtos = buildCommentList(comments);
        log.info("find comments by eventId={}", eventId);
        return commentDtos;
    }

    @GetMapping("/{commentId}")
    public CommentDto findCommentById(@PathVariable long commentId) {
        Comment comment = publicCommentService.findCommentById(commentId);
        CommentDto commentDto = toCommentDto(comment);
        commentDto.setUser(toUserDto(comment.getUser()));
        commentDto.setEvent(toEventShortDto(comment.getEvent()));
        log.info("return comment by commentId={}: {}", commentId, commentDto);
        return commentDto;
    }
}
