package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.service.NoteServiceImpl; // Używamy implementacji dla getCurrentUser()
import jakarta.validation.Valid; // Dla @Valid, jeśli dodasz walidację
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteServiceImpl noteService; // Używamy NoteServiceImpl, aby mieć dostęp do getCurrentUser()

    @Autowired
    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String listNotes(Model model) {
        User currentUser = noteService.getCurrentUser();
        List<Note> notes = noteService.findAllNotesByUser(currentUser);
        model.addAttribute("notes", notes);
        return "notes/list"; // Widok: src/main/resources/templates/notes/list.html
    }

    @GetMapping("/new")
    public String showCreateNoteForm(Model model) {
        model.addAttribute("note", new Note()); // Pusty obiekt Note dla formularza
        model.addAttribute("formTitle", "Nowa notatka");
        model.addAttribute("actionUrl", "/notes/save"); // Akcja formularza
        model.addAttribute("activePage", "notes");
        return "notes/form"; // Widok: src/main/resources/templates/notes/form.html
    }

    @GetMapping("/edit/{id}")
    public String showEditNoteForm(@PathVariable("id") Long id, Model model) {
        User currentUser = noteService.getCurrentUser();
        Optional<Note> noteOptional = noteService.findNoteByIdAndUser(id, currentUser);
        if (noteOptional.isPresent()) {
            model.addAttribute("note", noteOptional.get());
            model.addAttribute("formTitle", "Edytuj notatkę");
            model.addAttribute("actionUrl", "/notes/save/" + id); // Akcja formularza z ID
            model.addAttribute("activePage", "notes");
            return "notes/form";
        } else {
            // Notatka nie znaleziona lub brak dostępu
            return "redirect:/notes?error=notFoundOrAccessDenied";
        }
    }

    @PostMapping("/save") // Zapis nowej notatki
    public String saveNewNote(@Valid @ModelAttribute("note") Note note, BindingResult result, Model model) {
        User currentUser = noteService.getCurrentUser();
        if (result.hasErrors()) {
            // Jeśli są błędy walidacji, wróć do formularza
            model.addAttribute("formTitle", "Nowa notatka");
            model.addAttribute("actionUrl", "/notes/save");
            model.addAttribute("activePage", "notes");
            return "notes/form";
        }
        noteService.saveNote(note, currentUser);
        return "redirect:/notes";
    }

    @PostMapping("/save/{id}") // Aktualizacja istniejącej notatki
    public String updateExistingNote(@PathVariable("id") Long id, @Valid @ModelAttribute("note") Note note,
                                     BindingResult result, Model model) {
        User currentUser = noteService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Edytuj notatkę");
            model.addAttribute("actionUrl", "/notes/save/" + id);
            model.addAttribute("activePage", "notes");
            return "notes/form";
        }

        // Sprawdź, czy notatka do edycji faktycznie należy do użytkownika
        Optional<Note> existingNoteOpt = noteService.findNoteByIdAndUser(id, currentUser);
        if (existingNoteOpt.isPresent()) {
            Note existingNote = existingNoteOpt.get();
            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            // Daty createdAt i updatedAt są zarządzane przez @PrePersist i @PreUpdate
            noteService.saveNote(existingNote, currentUser); // Zapisz zaktualizowaną notatkę
        } else {
            // Obsługa sytuacji, gdy notatka nie istnieje lub nie należy do użytkownika
            return "redirect:/notes?error=notFoundOrAccessDeniedDuringUpdate";
        }
        return "redirect:/notes";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        User currentUser = noteService.getCurrentUser();
        noteService.deleteNoteByIdAndUser(id, currentUser);
        return "redirect:/notes";
    }
}