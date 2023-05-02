package ru.tinkoff.edu.java.scrapper.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.database.entity.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Link findByUrl(String url);

    List<Link> findAllByCheckedAtIsLessThanEqual(OffsetDateTime dateTime);

    @Modifying
    @Transactional
    @Query(value = "update links set checked_at = now() where url = ?", nativeQuery = true)
    void updateTime(String url);
}
