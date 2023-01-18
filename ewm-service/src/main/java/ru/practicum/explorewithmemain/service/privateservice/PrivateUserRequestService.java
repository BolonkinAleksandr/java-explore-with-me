package ru.practicum.explorewithmemain.service.privateservice;

import ru.practicum.explorewithmemain.dto.ParticipationDto;

import java.util.List;

public interface PrivateUserRequestService {

    List<ParticipationDto> getUserRequests(Long userId);

    ParticipationDto addUserRequest(Long userId, Long eventId);

    ParticipationDto rejectUserRequestById(Long userId, Long requestId);
}
