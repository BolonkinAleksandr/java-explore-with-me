package ru.practicum.explorewithmestats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithmestats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select e from EndpointHit e where e.uri = ?1 and e.timestamp between ?2 and ?3 ")
    Optional<List<EndpointHit>> findEndpointHitByUriUniqueFalse(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("select distinct e from EndpointHit e where e.uri = ?1 and e.timestamp between ?2 and ?3 ")
    Optional<List<EndpointHit>> findEndpointHitByUriUniqueTrue(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("select distinct e from EndpointHit e where e.timestamp between ?2 and ?3 ")
    Optional<List<EndpointHit>> findEndpointHitByNotUriUniqueTrue(LocalDateTime start, LocalDateTime end);

    @Query("select e from EndpointHit e where e.timestamp between ?2 and ?3 ")
    Optional<List<EndpointHit>> findEndpointHitByNotUriUniqueFalse(LocalDateTime start, LocalDateTime end);

    @Query("select Count(e.uri) from EndpointHit e where e.uri=?1 ")
    Long countUri(String uri);
}
