package ru.practicum.explorewithmemain.service.publicservice;

import ru.practicum.explorewithmemain.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilation(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
