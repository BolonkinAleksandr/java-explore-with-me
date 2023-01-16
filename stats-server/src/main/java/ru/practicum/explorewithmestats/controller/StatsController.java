package ru.practicum.explorewithmestats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmestats.exception.NotFoundException;
import ru.practicum.explorewithmestats.model.EndpointHit;
import ru.practicum.explorewithmestats.model.ViewStats;
import ru.practicum.explorewithmestats.service.StatsService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit addEndpointHit(@RequestBody EndpointHit endpointHit) {
        log.info("add endpointHit={}", endpointHit);
        return statsService.addEndpointHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(@RequestParam(name = "start") Optional<String> start,
                                        @RequestParam(name = "end") Optional<String> end,
                                        @RequestParam(name = "uris") Optional<List<String>> uris,
                                        @RequestParam(name = "unique", defaultValue = "false") Boolean unique)
            throws NotFoundException {
        log.info("get view stats start={}, end={}, uris {}, unique={}",
                start, end, uris, unique);
        return statsService.getListViewStats(start, end, uris, unique);
    }
}
