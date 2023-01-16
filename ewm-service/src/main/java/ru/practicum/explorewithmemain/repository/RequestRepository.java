package ru.practicum.explorewithmemain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithmemain.model.Participation;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Participation, Long> {

    @Query("select r from Participation r " +
            " JOIN Event e ON r.event.id = e.id" +
            " where e.id = ?1 and e.initiator.id = ?2 ")
    Optional<List<Participation>> findRequestUserByIdAndEventById(Long eventId, Long userId);

    @Query("select r from Participation r where r.requester.id = ?1")
    Optional<List<Participation>> findAllRequestUserById(Long id);

    @Query("select r from Participation r where r.requester.id = ?1 and r.event.id = ?2")
    Optional<Participation> findRequestByUserIdAndEventId(Long userId, Long eventId);

    @Query("select r from Participation r where r.requester.id = ?1 and r.id = ?2 and r.status <> 'CONFIRMED'")
    Optional<Participation> findRequestById(Long userId, Long id);
}
