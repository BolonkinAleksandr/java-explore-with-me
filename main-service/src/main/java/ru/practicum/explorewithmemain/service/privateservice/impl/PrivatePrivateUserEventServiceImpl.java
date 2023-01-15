package ru.practicum.explorewithmemain.service.privateservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.*;
import ru.practicum.explorewithmemain.exceptions.ForbiddenException;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.exceptions.RequestException;
import ru.practicum.explorewithmemain.mapper.EventMapper;
import ru.practicum.explorewithmemain.mapper.ParticipationMapper;
import ru.practicum.explorewithmemain.model.*;
import ru.practicum.explorewithmemain.repository.*;
import ru.practicum.explorewithmemain.service.privateservice.PrivateUserEventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivatePrivateUserEventServiceImpl implements PrivateUserEventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        final Pageable pageable = CustomPageable.of(from, size);
        List<Event> listEvent = eventRepository.findEventsByUserId(userId, pageable).getContent();
        if (listEvent.isEmpty()) {
            throw new NotFoundException("object doesn't found ",
                    String.format("ListEvents with userId={} was not found.", userId));
        }
        log.info("get user's events userId={}", userId);
        return EventMapper.toListEventShortDto(listEvent);
    }

    @Transactional
    @Override
    public EventFullDto updateUserIdEvent(Long userId, UpdateEventRequest updateEventRequest) {
        userValidation(userId);
        Event event = eventRepository
                .findUserEventById(userId, updateEventRequest.getEventId())
                .orElseThrow(() -> new NotFoundException("object doesn't found ",
                        String.format("Event with id={} userId={} was not found.",
                                updateEventRequest.getEventId(), userId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("invalid ", "Initiator");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new RequestException("error with request ", "State");
        }
        if (event.getState().equals(State.CANCELED)) {
            event.setRequestModeration(true);
        }
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RequestException("error with request ", "EventRequest");
        }
        event.setEventDate(updateEventRequest.getEventDate());
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository
                    .findById(updateEventRequest.getCategory()).orElseThrow(
                            () -> new NotFoundException("object doesn't found ",
                                    String.format("Category with id={} was not found.",
                                            updateEventRequest.getCategory()))));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        event.setState(State.PENDING);
        eventRepository.saveAndFlush(event);
        log.info("update user's event userId={}", userId);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto addUserEvent(Long userId, NewEventDto newEventDto) {
        User user = userValidation(userId);
        if (locationRepository.findByLatAndLon(newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon()).isPresent()) {
            throw new RequestException("error with request ", "locationRepository");
        }
        if (!newEventDto.getEventDate().minusHours(2).isAfter(LocalDateTime.now())) {
            throw new RequestException("error with request ", "EventDate");
        }
        Category category = categoryRepository
                .findById(newEventDto.getCategory()).orElseThrow(() ->
                        new NotFoundException("object doesn't found ",
                                String.format("Category with id={} was not found.",
                                        newEventDto.getCategory())));
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setInitiator(user);
        locationRepository.save(newEventDto.getLocation());
        eventRepository.save(event);
        log.info("add event eventId={}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        userValidation(userId);
        Event event = eventRepository
                .findUserEventById(userId, eventId).orElseThrow(() ->
                        new NotFoundException("object doesn't found ",
                                String.format("Event with eventId={} and userId={} was not found.",
                                        eventId, userId)));
        log.info("get event information userId={}", userId);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto rejectEvent(Long userId, Long eventId) {
        userValidation(userId);
        Event event = eventRepository
                .findById(eventId).orElseThrow(() ->
                        new NotFoundException("object doesn't found ",
                                String.format("Event with eventId={} and userId={} was not found.",
                                        eventId, userId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("invalid ", "Initiator");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new RequestException("error with request ", "State");
        }
        log.info("сancel event eventId={} by user userId={}", eventId, userId);
        event.setState(State.CANCELED);
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationDto> getUserEventRequestsById(Long userId, Long eventId) {
        List<Participation> participation =
                requestRepository.findRequestUserByIdAndEventById(eventId, userId)
                        .orElseThrow(() -> new NotFoundException("object doesn't found ",
                                String.format("ParticipationRequest with userId={}, eventId={} was not found.",
                                        userId, eventId)));
        log.info("get event information by user userId={}", userId);
        return ParticipationMapper.toListParticipationRequestDto(participation);
    }

    @Transactional
    @Override
    public ParticipationDto confirmRequest(Long userId, Long eventId, Long reqId) {
        userValidation(userId);
        Event event = eventValidation(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestException("error with request ", "incorrect userId");
        }
        Participation participation = requestRepository
                .findById(reqId).orElseThrow(() -> new NotFoundException("object doesn't found ",
                        String.format("ParticipationRequest with reqId={} was not found.", reqId)));
        if (!participation.getEvent().getId().equals(eventId)) {
            throw new RequestException("error with request ", "incorrect eventId");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return ParticipationMapper.toParticipationRequestDto(participation);
        }
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ForbiddenException("invalid ", "ParticipantLimit");
        }

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        participation.setStatus(Status.CONFIRMED);
        requestRepository.save(participation);
        log.info("confirm request userId={}", userId);
        return ParticipationMapper.toParticipationRequestDto(participation);
    }

    @Transactional
    @Override
    public ParticipationDto rejectRequest(Long userId, Long eventId, Long reqId) {
        userValidation(userId);
        if (!eventValidation(eventId).getInitiator().getId().equals(userId)) {
            throw new RequestException("error with request ", "incorrect userId");
        }
        Participation participation = requestRepository
                .findById(reqId).orElseThrow(() -> new NotFoundException("object doesn't found ",
                        String.format("ParticipationRequest with reqId={} was not found.", reqId)));
        if (!participation.getEvent().getId().equals(eventId)) {
            throw new RequestException("error with request ", "incorrect eventId");
        }

        participation.setStatus(Status.REJECTED);
        requestRepository.save(participation);
        log.info("reject request userId={}", userId);
        return ParticipationMapper.toParticipationRequestDto(participation);
    }

    private Event eventValidation(Long eventId) throws NotFoundException {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("Event with id={} was not found.", eventId)));
    }

    private User userValidation(Long userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("User with id={} was not found.", userId)));
    }
}
