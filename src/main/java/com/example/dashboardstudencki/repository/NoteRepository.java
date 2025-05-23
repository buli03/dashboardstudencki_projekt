package com.example.dashboardstudencki.repository;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserOrderByUpdatedAtDesc(User user);
    Optional<Note> findByIdAndUser(Long id, User user);

    @Query("SELECT n FROM Note n WHERE n.user = :user " +
            "AND ( (:searchTerm IS NULL OR :searchTerm = '') OR " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
            "(n.content LIKE CONCAT('%', :searchTerm, '%')) )")
    List<Note> searchNotesForUser(
            @Param("user") User user,
            @Param("searchTerm") String searchTerm,
            Sort sort
    );

    long countByUser(User user);
}