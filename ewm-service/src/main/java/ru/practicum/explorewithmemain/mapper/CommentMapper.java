package ru.practicum.explorewithmemain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmemain.dto.*;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                new UserDto(),
                comment.getCreated(),
                new EventShortDto(),
                comment.getStatus()
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                new User(),
                commentDto.getCreated(),
                new Event(),
                commentDto.getStatus()
        );
    }

    public static Comment toComment(NewCommentDto commentDto) {
        return new Comment(
                0,
                commentDto.getText(),
                new User(),
                LocalDateTime.now(),
                new Event(),
                State.PENDING
        );
    }

    public static Comment toComment(UpdateCommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                new User(),
                LocalDateTime.now(),
                new Event(),
                State.PENDING
        );
    }
}
