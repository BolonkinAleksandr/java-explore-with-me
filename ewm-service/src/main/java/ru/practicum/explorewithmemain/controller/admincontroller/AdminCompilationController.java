package ru.practicum.explorewithmemain.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.dto.CompilationDto;
import ru.practicum.explorewithmemain.dto.NewCompilationDto;
import ru.practicum.explorewithmemain.service.adminservice.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("add compilation newCompilationDto {}", newCompilationDto);
        return adminCompilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("delete compilation compId={}", compId);
        adminCompilationService.deleteCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId,
                                         @PathVariable Long eventId) {
        log.info("delete event in compilation compId={}, eventId={}", compId, eventId);
        adminCompilationService.deleteEventByIdFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void updateEventInCompilation(@PathVariable Long compId,
                                         @PathVariable Long eventId) {
        log.info("update event in compilation compId={}, eventId={}", compId, eventId);
        adminCompilationService.updateEventInCompilationById(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("unpin compilation compId={}", compId);
        adminCompilationService.unpinCompilationById(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("pin compilation compId={}", compId);
        adminCompilationService.pinCompilationById(compId);
    }
}
