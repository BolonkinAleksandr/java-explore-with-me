package ru.practicum.explorewithmemain.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.dto.ParticipationDto;
import ru.practicum.explorewithmemain.service.privateservice.PrivateUserRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class PrivateUserRequestController {

    private final PrivateUserRequestService privateUserRequestService;

    @GetMapping
    public List<ParticipationDto> getUserRequests(@PathVariable Long userId) {
        log.info("get user request by userId={}", userId);
        return privateUserRequestService.getUserRequests(userId);
    }

    @PostMapping
    public ParticipationDto addUserRequest(@PathVariable Long userId,
                                           @RequestParam Long eventId) {
        log.info("create request userId={}, eventId={}", userId, eventId);
        return privateUserRequestService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationDto rejectUserRequest(@PathVariable Long userId,
                                              @PathVariable Long requestId) {
        log.info("cancel user request userId={}, requestId={}",
                userId, requestId);
        return privateUserRequestService.rejectUserRequestById(userId, requestId);
    }
}
