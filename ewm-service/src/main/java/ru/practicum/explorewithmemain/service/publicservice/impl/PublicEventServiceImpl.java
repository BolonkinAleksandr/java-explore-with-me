package ru.practicum.explorewithmemain.service.publicservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.client.RestTemplateClientStat;
import ru.practicum.explorewithmemain.dto.EndpointHitDto;
import ru.practicum.explorewithmemain.dto.EventFullDto;
import ru.practicum.explorewithmemain.dto.EventShortDto;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.EventMapper;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.service.publicservice.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final RestTemplateClientStat restTemplateClientStat;
    private final EventRepository eventRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> getEventsByTextAndCategory(
            String text, List<Long> categories,
            Boolean paid, String rangeStart,
            String rangeEnd, boolean onlyAvailable,
            String sort, Integer from, Integer size,
            HttpServletRequest request) {
        EndpointHitDto endpointHit = new EndpointHitDto();
        endpointHit.setApp("ewm-main-service");
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(LocalDateTime.now());
        restTemplateClientStat.createEndpointHitStatistics(endpointHit);

        List<Event> listEvent = new ArrayList<>();
        final Pageable pageable = CustomPageable.of(from, size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(formatter);
        }

        if (sort.equals("EVENT_DATE") && !onlyAvailable && rangeEnd == null) {
            listEvent = eventRepository
                    .searchEventByEventDayAvailableFalseEndNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), pageable).getContent();
        }

        if (sort.equals("EVENT_DATE") && !onlyAvailable && rangeEnd != null) {
            listEvent = eventRepository
                    .searchEventByEventDayAvailableFalseEndNotNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        }

        if (sort.equals("EVENT_DATE") && onlyAvailable && rangeEnd == null) {
            listEvent = eventRepository
                    .searchEventByEventDayAvailableTrueEndNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), pageable).getContent();
        }

        if (sort.equals("EVENT_DATE") && onlyAvailable && rangeEnd != null) {
            listEvent = eventRepository
                    .searchEventByEventDayAvailableTrueEndNotNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        }

        if (sort.equals("VIEWS") && !onlyAvailable && rangeEnd == null) {
            listEvent = eventRepository
                    .searchEventByViewsAvailableFalseEndNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), pageable).getContent();
        }

        if (sort.equals("VIEWS") && !onlyAvailable && rangeEnd != null) {
            listEvent = eventRepository
                    .searchEventByViewsAvailableFalseEndNotNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        }
        if (sort.equals("VIEWS") && onlyAvailable && rangeEnd == null) {
            listEvent = eventRepository
                    .searchEventByViewsAvailableTrueEndNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), pageable).getContent();
        }

        if (sort.equals("VIEWS") && onlyAvailable && rangeEnd != null) {
            listEvent = eventRepository
                    .searchEventByViewsAvailableTrueEndNotNull(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        }

        if (listEvent.isEmpty()) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Events with text = {} was not found.", text));
        }
        return EventMapper.toListEventShortDto(listEvent);
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("object doesn't found ",
                        String.format("Events with id={} was not found.", id)));
        event.setViews(event.getViews() + 1);
        eventRepository.saveAndFlush(event);
        EndpointHitDto endpointHit = new EndpointHitDto();
        endpointHit.setApp("ewm-main-service");
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(LocalDateTime.now());
        restTemplateClientStat.createEndpointHitStatistics(endpointHit);
        return EventMapper.toEventFullDto(event);
    }
}
