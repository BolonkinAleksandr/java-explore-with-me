package ru.practicum.explorewithmemain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmemain.model.Status;
import ru.practicum.explorewithmemain.util.Constants;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationDto {
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private Status status;
}
