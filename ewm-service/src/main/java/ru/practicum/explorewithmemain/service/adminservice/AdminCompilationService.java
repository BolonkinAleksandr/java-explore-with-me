package ru.practicum.explorewithmemain.service.adminservice;

import ru.practicum.explorewithmemain.dto.CompilationDto;
import ru.practicum.explorewithmemain.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Long compId);

    void deleteEventByIdFromCompilation(Long compId, Long eventId);

    void updateEventInCompilationById(Long compId, Long eventId);

    void unpinCompilationById(Long compId);

    void pinCompilationById(Long compId);
}