package com.example.dashboardstudencki.repository;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Znajdź wszystkie notatki dla danego użytkownika, posortowane np. po dacie modyfikacji (malejąco)
    List<Note> findByUserOrderByUpdatedAtDesc(User user);

    // Znajdź notatkę po ID i użytkowniku (dla bezpieczeństwa)
    Optional<Note> findByIdAndUser(Long id, User user);
}