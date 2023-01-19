package ru.practicum.explorewithmemain.util;

import ru.practicum.explorewithmemain.dto.CommentDto;
import ru.practicum.explorewithmemain.model.Comment;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.explorewithmemain.mapper.CommentMapper.toCommentDto;
import static ru.practicum.explorewithmemain.mapper.EventMapper.toEventShortDto;
import static ru.practicum.explorewithmemain.mapper.UserMapper.toUserDto;

public class Methods {
    public static List<CommentDto> buildCommentList(List<Comment> comments){
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            CommentDto commentDto = toCommentDto(c);
            commentDto.setUser(toUserDto(c.getUser()));
            commentDto.setEvent(toEventShortDto(c.getEvent()));
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }
}
