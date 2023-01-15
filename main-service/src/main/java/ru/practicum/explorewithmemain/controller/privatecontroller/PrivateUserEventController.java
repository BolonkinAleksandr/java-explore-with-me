package ru.practicum.explorewithmemain.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.dto.*;
import ru.practicum.explorewithmemain.service.privateservice.PrivateUserEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PrivateUserEventController {
    private final PrivateUserEventService privateUserEventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(name = "from",
                                                     defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size",
                                                     defaultValue = "10") Integer size) {
        log.info("get categories with userId={}, from={}, size={}",
                userId, from, size);
        return privateUserEventService.getUserEvents(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable Long userId,
                                        @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("patch user event with userId={}, updateEventRequest {}",
                userId, updateEventRequest);
        return privateUserEventService.updateUserIdEvent(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        log.info("create event with userId={}, newEventDto {}", userId, newEventDto);
        return privateUserEventService.addUserEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("get user event by userId={}, eventId={}", userId, eventId);
        return privateUserEventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto сancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("сancel event with userId={}, eventId={}", userId, eventId);
        return privateUserEventService.сancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationDto> getUserEventRequests(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        log.info("get requests user event by userId={}, eventId={}",
                userId, eventId);
        return privateUserEventService.getUserEventRequestsById(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationDto confirmRequest(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @PathVariable Long reqId) {
        log.info("confirm request userId={}, eventId={}, reqId={}",
                userId, eventId, reqId);
        return privateUserEventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationDto rejectRequest(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @PathVariable Long reqId) {
        log.info("reject request userId={}, eventId={}, reqId={}",
                userId, eventId, reqId);
        return privateUserEventService.rejectRequest(userId, eventId, reqId);
    }
}
