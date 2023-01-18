package ru.practicum.explorewithmemain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmemain.util.Constants;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = Constants.DATA_TIME_PATTERN)
    private LocalDateTime timestamp;
    private Long hits;
}
