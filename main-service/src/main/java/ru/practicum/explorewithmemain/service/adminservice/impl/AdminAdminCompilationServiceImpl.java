package ru.practicum.explorewithmemain.service.adminservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.CompilationDto;
import ru.practicum.explorewithmemain.dto.NewCompilationDto;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.CompilationMapper;
import ru.practicum.explorewithmemain.model.Compilation;
import ru.practicum.explorewithmemain.model.Event;
import ru.practicum.explorewithmemain.repository.CompilationRepository;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.service.adminservice.AdminCompilationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAdminCompilationServiceImpl implements AdminCompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (!newCompilationDto.getEvents().isEmpty()) {
            for (Long id : newCompilationDto.getEvents()) {
                compilation.getEvents().add(eventRepository.findById(id).get());
            }
        }
        compilationRepository.save(compilation);
        log.info("add compilation id={}", compilation.getId());
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public void updateEventInCompilationById(Long compId, Long eventId) {
        Compilation compilation = compilationRepository
                .findById(compId).orElseThrow(() -> new NotFoundException("object not found ",
                        String.format("Compilation with id={} was not found.", compId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("object not found ",
                        String.format("Event with id={} was not found.", eventId)));
        compilation.getEvents().add(event);
        compilationRepository.saveAndFlush(compilation);
        log.info("add event eventId={} to compilation compilationId={}", eventId, compId);
    }

    @Transactional
    @Override
    public void unpinCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("object not found ",
                        String.format("Compilation with id={} was not found.", compId)));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("unpin compilation compilationId={}", compId);
    }

    @Transactional
    @Override
    public void pinCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Объект не найден. ",
                        String.format("Compilation with id={} was not found.", compId)));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("pin compilation compilationId={}", compId);
    }

    @Transactional
    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("delete compilation compilationId={}", compId);
    }

    @Transactional
    @Override
    public void deleteEventByIdFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository
                .findById(compId).orElseThrow(() -> new NotFoundException("object not found ",
                        String.format("Compilation with id={} was not found.", compId)));
        compilation.getEvents().removeIf(e -> (e.getId().equals(eventId)));
        compilationRepository.saveAndFlush(compilation);
        log.info("delete event eventId={} from compilation compilationId={}", eventId, compId);
    }
}
