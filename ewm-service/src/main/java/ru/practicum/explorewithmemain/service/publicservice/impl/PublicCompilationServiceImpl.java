package ru.practicum.explorewithmemain.service.publicservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmemain.dto.CompilationDto;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.CompilationMapper;
import ru.practicum.explorewithmemain.model.Compilation;
import ru.practicum.explorewithmemain.repository.CompilationRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.service.publicservice.PublicCompilationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilation(boolean pinned, Integer from, Integer size) {
        final Pageable pageable = CustomPageable.of(from, size);
        List<Compilation> listCompilation;
        if (pinned) {
            listCompilation = compilationRepository
                    .findByPinned(pinned, pageable).getContent();
        } else {
            listCompilation = compilationRepository.findAll(pageable).getContent();
        }
        return CompilationMapper.toListCompilationDto(listCompilation);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("Compilation with id={} was not found.", compId)));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
