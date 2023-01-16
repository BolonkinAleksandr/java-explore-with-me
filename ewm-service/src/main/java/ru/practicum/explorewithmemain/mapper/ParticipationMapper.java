package ru.practicum.explorewithmemain.mapper;

import ru.practicum.explorewithmemain.dto.ParticipationDto;
import ru.practicum.explorewithmemain.model.Participation;

import java.util.ArrayList;
import java.util.List;

public class ParticipationMapper {

    public static ParticipationDto toParticipationRequestDto(Participation request) {
        return new ParticipationDto(request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public static List<ParticipationDto> toListParticipationRequestDto(List<Participation> list) {
        List<ParticipationDto> listDto = new ArrayList<>();
        for (Participation request : list) {
            listDto.add(toParticipationRequestDto(request));
        }
        return listDto;
    }
}
