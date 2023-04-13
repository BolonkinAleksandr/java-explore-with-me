package ru.practicum.explorewithmemain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmemain.model.Comment;
import ru.practicum.explorewithmemain.model.State;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(long userId, Pageable page);

    List<Comment> findByEventId(long userId, Pageable page);

    List<Comment> findByEventIdAndStatus(long eventId, State status, Pageable page);

    @Query("SELECT c FROM Comment AS c WHERE c.text LIKE CONCAT('%',LOWER(:text),'%')")
    List<Comment> findCommentsByTextContainingIgnoreCase(String text, Pageable page);
}
