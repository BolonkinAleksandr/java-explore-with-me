package ru.practicum.explorewithmestats.service;

import ru.practicum.explorewithmestats.exception.NotFoundException;
import ru.practicum.explorewithmestats.model.EndpointHit;
import ru.practicum.explorewithmestats.model.ViewStats;

import java.util.List;
import java.util.Optional;

public interface StatsService {

    EndpointHit addEndpointHit(EndpointHit endpointHit);

    List<ViewStats> getListViewStats(Optional<String> start, Optional<String> end,
                                     Optional<List<String>> uris, Boolean unique) throws NotFoundException;
}