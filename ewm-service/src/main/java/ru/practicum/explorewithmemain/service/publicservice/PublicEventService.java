package ru.practicum.explorewithmemain.service.publicservice;

import ru.practicum.explorewithmemain.dto.EventFullDto;
import ru.practicum.explorewithmemain.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {

    List<EventShortDto> getEventsByTextAndCategory(
            String text, List<Long> categories,
            Boolean rangeStart, String rangeEnd, String paid, boolean onlyAvailable, String sort,
            Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);
}
