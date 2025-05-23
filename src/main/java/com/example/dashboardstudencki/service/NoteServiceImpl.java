package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.NoteRepository;
import com.example.dashboardstudencki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // Dodaj ten import
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Dodaj ten import

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAllNotesByUser(User user) {
        // Możemy domyślnie użyć nowej metody lub zostawić starą.
        // Na razie wywołamy nową z domyślnym sortowaniem i brakiem wyszukiwania.
        return searchAndSortNotes(user, null, "updatedAt", "desc");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findNoteByIdAndUser(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user);
    }

    @Override
    public Note saveNote(Note note, User user) {
        if (note.getUser() == null || !note.getUser().getId().equals(user.getId())) {
            note.setUser(user);
        }
        return noteRepository.save(note);
    }

    @Override
    public void deleteNoteByIdAndUser(Long id, User user) {
        Optional<Note> noteOptional = noteRepository.findByIdAndUser(id, user);
        noteOptional.ifPresent(noteRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchAndSortNotes(User user, String searchTerm, String sortBy, String sortDirection) {
        // Domyślne sortowanie, jeśli nie podano parametrów
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt"); // Domyślnie po dacie aktualizacji malejąco

        if (StringUtils.hasText(sortBy) && StringUtils.hasText(sortDirection)) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            sort = Sort.by(direction, sortBy);
        } else if (StringUtils.hasText(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, sortBy); // Domyślnie ASC, jeśli tylko sortBy jest podane
        }

        String normalizedSearchTerm = StringUtils.hasText(searchTerm) ? searchTerm : null;

        return noteRepository.searchNotesForUser(user, normalizedSearchTerm, sort);
    }

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