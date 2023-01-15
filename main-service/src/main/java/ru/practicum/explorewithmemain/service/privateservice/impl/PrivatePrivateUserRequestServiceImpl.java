package ru.practicum.explorewithmemain.service.privateservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.ParticipationDto;
import ru.practicum.explorewithmemain.exceptions.ForbiddenException;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.ParticipationMapper;
import ru.practicum.explorewithmemain.model.*;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.repository.RequestRepository;
import ru.practicum.explorewithmemain.repository.UserRepository;
import ru.practicum.explorewithmemain.service.privateservice.PrivateUserRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivatePrivateUserRequestServiceImpl implements PrivateUserRequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public ParticipationDto addUserRequest(Long userId, Long eventId) {
        if (requestRepository.findRequestByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException("invalid ", "requestRepository");
        }
        User user = userValidation(userId);
        Event event = eventValidation(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("invalid ", "Initiator");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ForbiddenException("invalid ", "State");
        }
        if (event.getParticipantLimit() != 0
                && (event.getParticipantLimit() - event.getConfirmedRequests() <= 0)) {
            throw new ForbiddenException("invalid ", "ParticipantLimit");
        }
        Participation participation = new Participation();
        if (!event.getRequestModeration()) {
            participation.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            participation.setStatus(Status.PENDING);
        }
        participation.setRequester(user);
        participation.setCreated(LocalDateTime.now());
        participation.setEvent(event);
        requestRepository.save(participation);
        log.info("add request by user userId={}", userId);
        return ParticipationMapper.toParticipationRequestDto(participation);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationDto> getUserRequests(Long userId) {
        List<Participation> listRequest = requestRepository
                .findAllRequestUserById(userId).orElseThrow(() ->
                        new NotFoundException("object doesn't found ",
                                String.format("ParticipationRequest list with userId={} was not found.", userId)));
        log.info("ger request information userId={}", userId);
        return ParticipationMapper.toListParticipationRequestDto(listRequest);
    }

    @Transactional
    @Override
    public ParticipationDto rejectUserRequestById(Long userId, Long requestId) {
        Participation participation = requestRepository
                .findRequestById(userId, requestId).orElseThrow(() ->
                        new NotFoundException("object doesn't found ",
                                String.format("ParticipationRequest with reqId={} was not found.", requestId)));

        participation.setStatus(Status.CANCELED);
        requestRepository.save(participation);
        log.info("reject request requestId={} by user userId={}", requestId, userId);
        return ParticipationMapper.toParticipationRequestDto(participation);
    }

    private Event eventValidation(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("Event with id={} was not found.", eventId)));
    }

    private User userValidation(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("User with id={} was not found.", userId)));
    }
}
