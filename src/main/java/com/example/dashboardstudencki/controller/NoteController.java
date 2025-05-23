package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.model.Note;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.service.NoteServiceImpl;
import jakarta.validation.Valid;
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

    private final NoteServiceImpl noteService;

    @Autowired
    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String listNotes(Model model,
                            @RequestParam(value = "searchTerm", required = false) String searchTerm,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "updatedAt") String sortBy,
                            @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection) {

        User currentUser = noteService.getCurrentUser();
        List<Note> notes = noteService.searchAndSortNotes(currentUser, searchTerm, sortBy, sortDirection);

        model.addAttribute("notes", notes);
        model.addAttribute("activePage", "notes");

        // Przekazanie parametrów wyszukiwania/sortowania z powrotem do widoku
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);

        return "notes/list";
    }

    @GetMapping("/new")
    public String showCreateNoteForm(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("formTitle", "Nowa notatka");
        model.addAttribute("actionUrl", "/notes/save");
        model.addAttribute("activePage", "notes");
        return "notes/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditNoteForm(@PathVariable("id") Long id, Model model) {
        User currentUser = noteService.getCurrentUser();
        Optional<Note> noteOptional = noteService.findNoteByIdAndUser(id, currentUser);
        if (noteOptional.isPresent()) {
            model.addAttribute("note", noteOptional.get());
            model.addAttribute("formTitle", "Edytuj notatkę");
            model.addAttribute("actionUrl", "/notes/save/" + id);
            model.addAttribute("activePage", "notes");
            return "notes/form";
        } else {
            return "redirect:/notes?error=notFoundOrAccessDenied";
        }
    }

    @PostMapping("/save")
    public String saveNewNote(@Valid @ModelAttribute("note") Note note, BindingResult result, Model model) {
        User currentUser = noteService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Nowa notatka");
            model.addAttribute("actionUrl", "/notes/save");
            model.addAttribute("activePage", "notes");
            return "notes/form";
        }
        noteService.saveNote(note, currentUser);
        return "redirect:/notes";
    }

    @PostMapping("/save/{id}")
    public String updateExistingNote(@PathVariable("id") Long id, @Valid @ModelAttribute("note") Note note,
                                     BindingResult result, Model model) {
        User currentUser = noteService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Edytuj notatkę");
            model.addAttribute("actionUrl", "/notes/save/" + id);
            model.addAttribute("activePage", "notes");
            return "notes/form";
        }
        Optional<Note> existingNoteOpt = noteService.findNoteByIdAndUser(id, currentUser);
        if (existingNoteOpt.isPresent()) {
            Note existingNote = existingNoteOpt.get();
            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            noteService.saveNote(existingNote, currentUser);
        } else {
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