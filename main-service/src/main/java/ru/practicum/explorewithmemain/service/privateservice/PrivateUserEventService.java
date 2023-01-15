package ru.practicum.explorewithmemain.service.privateservice;

import ru.practicum.explorewithmemain.dto.*;

import java.util.List;

public interface PrivateUserEventService {

    EventFullDto getUserEventById(Long catId, Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto updateUserIdEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto addUserEvent(Long userId, NewEventDto newEventDto);

    List<ParticipationDto> getUserEventRequestsById(Long userId, Long eventId);

    ParticipationDto confirmRequest(Long userId, Long eventId, Long reqId);

    ParticipationDto rejectRequest(Long userId, Long eventId, Long reqId);

    EventFullDto сancelEvent(Long userId, Long eventId);
}
