package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    List<Note> findAllNotesByUser(User user);
    Optional<Note> findNoteByIdAndUser(Long id, User user);
    Note saveNote(Note note, User user);
    void deleteNoteByIdAndUser(Long id, User user);

    List<Note> searchAndSortNotes(User user, String searchTerm, String sortBy, String sortDirection);
}