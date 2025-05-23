package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.NoteRepository;
import com.example.dashboardstudencki.repository.UserRepository; // Potrzebne do getCurrentUser
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository; // Do pobierania aktualnego użytkownika

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAllNotesByUser(User user) {
        return noteRepository.findByUserOrderByUpdatedAtDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findNoteByIdAndUser(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user);
    }

    @Override
    public Note saveNote(Note note, User user) {
        // Upewniamy się, że notatka jest powiązana z prawidłowym użytkownikiem
        if (note.getUser() == null || !note.getUser().getId().equals(user.getId())) {
            note.setUser(user);
        }
        return noteRepository.save(note);
    }

    @Override
    public void deleteNoteByIdAndUser(Long id, User user) {
        Optional<Note> noteOptional = noteRepository.findByIdAndUser(id, user);
        noteOptional.ifPresent(noteRepository::delete);
        // Możesz dodać logikę obsługi, jeśli notatka nie zostanie znaleziona
    }

    // Pomocnicza metoda do pobrania aktualnie zalogowanego użytkownika
    // Powinna być identyczna jak w TaskServiceImpl lub wyekstrahowana do wspólnego serwisu.
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No authenticated user found");
        }
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));
    }
}