package ru.practicum.explorewithmemain.service.adminservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithmemain.dto.EventFullDto;
import ru.practicum.explorewithmemain.exceptions.ForbiddenException;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.EventMapper;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.model.State;
import ru.practicum.explorewithmemain.repository.CategoryRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.service.adminservice.AdminEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventFullDto> getEventsByUsersStatesCategories(
            List<Long> users, List<String> states, List<Long> categories,
            String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<State> listState = new ArrayList<>();
        List<Event> listEvents;
        if (states != null) {
            for (String state : states) {
                listState.add(State.valueOf(state));
            }
        }

        final Pageable pageable = CustomPageable.of(from, size);

        if (states == null && rangeStart == null && rangeEnd == null) {
            listEvents = eventRepository
                    .searchEventsByAdminWithOutStatesAndRange(users, categories, pageable).getContent();
        } else if (users == null) {
            listEvents = eventRepository
                    .searchEventsNotUsersGetConditions(
                            listState, categories,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();

        } else if (categories == null) {
            listEvents = eventRepository
                    .searchEventsNotCategoriesGetConditions(
                            users, listState,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        } else {
            listEvents = eventRepository
                    .searchEventsByAdminGetConditions(
                            users, listState, categories,
                            LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), pageable).getContent();
        }

        if (listEvents.isEmpty()) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Event list was not found."));
        }
        return EventMapper.toListEventFullDto(listEvents);
    }

    private Event eventValidation(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("object doesn't found ",
                String.format("Event with id={} was not found.", eventId)));
    }

    @Override
    public EventFullDto updateEventById(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventValidation(eventId);
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("object doesn't found ",
                            String.format("Category id={} was not found.", adminUpdateEventRequest.getCategory()))));
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        eventRepository.saveAndFlush(event);
        log.info("search event eventId={}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto publishEventById(Long eventId) {
        Event event = eventValidation(eventId);
        if (event.getState() != State.PENDING) {
            throw new ForbiddenException("invalid ", "State");
        }
        event.setPublishedOn(LocalDateTime.now());
        if (event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            throw new ForbiddenException("invalid ", "EventDate");
        }
        event.setState(State.PUBLISHED);
        eventRepository.saveAndFlush(event);
        log.info("publish event eventId={}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto rejectEventById(Long eventId) {
        Event event = eventValidation(eventId);
        if (event.getState() != State.PENDING) {
            throw new ForbiddenException("invalid ", "State");
        }
        event.setState(State.CANCELED);
        eventRepository.saveAndFlush(event);
        log.info("reject event userId={}", eventId);
        return EventMapper.toEventFullDto(event);
    }
}
