package ru.practicum.explorewithmemain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.util.Constants;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private boolean paid;
    private Long participantLimit;
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
