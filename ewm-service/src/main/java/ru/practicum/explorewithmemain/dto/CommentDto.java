package ru.practicum.explorewithmemain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.util.Constants;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotNull
    private Long id;
    private String text;
    private UserDto user;
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime created;
    private EventShortDto event;
    private State status;
}