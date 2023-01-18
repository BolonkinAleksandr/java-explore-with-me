package ru.practicum.explorewithmemain.service.adminservice;

import ru.practicum.explorewithmemain.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithmemain.dto.EventFullDto;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getEventsByUsersStatesCategories(
            List<Long> users, List<String> states, List<Long> categories,
            String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEventById(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishEventById(Long eventId);

    EventFullDto rejectEventById(Long eventId);
}
