package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.service.TaskServiceImpl; // Zmienione na TaskServiceImpl dla getCurrentUser
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Dodaj import
import org.springframework.web.bind.annotation.*;  // Dodaj import dla @PathVariable, @ModelAttribute
import jakarta.validation.Valid; // Dodaj import dla @Valid (jeśli będziesz używać walidacji)

import java.time.LocalDateTime; // Dodaj import
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks") // Wszystkie żądania do /tasks będą obsługiwane przez ten kontroler
public class TaskController {

    private final TaskServiceImpl taskService; // Zmienione na TaskServiceImpl dla getCurrentUser

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    // Lista predefiniowanych priorytetów i statusów
    private final List<String> priorities = Arrays.asList("Niski", "Średni", "Wysoki");
    private final List<String> statuses = Arrays.asList("Do zrobienia", "W trakcie", "Zakończone");

    @GetMapping
    public String listTasks(Model model) {
        User currentUser = taskService.getCurrentUser();
        List<Task> tasks = taskService.findAllTasksByUser(currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("activePage", "tasks");
        return "tasks/list"; // Widok: src/main/resources/templates/tasks/list.html
    }

    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task()); // Pusty obiekt Task dla formularza
        model.addAttribute("priorities", priorities);
        model.addAttribute("statuses", statuses);
        model.addAttribute("formTitle", "Nowe zadanie");
        model.addAttribute("actionUrl", "/tasks/save");
        model.addAttribute("activePage", "tasks");
        return "tasks/form"; // Widok: src/main/resources/templates/tasks/form.html
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        User currentUser = taskService.getCurrentUser();
        Optional<Task> taskOptional = taskService.findTaskByIdAndUser(id, currentUser);
        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("priorities", priorities);
            model.addAttribute("statuses", statuses);
            model.addAttribute("formTitle", "Edytuj zadanie");
            model.addAttribute("actionUrl", "/tasks/save/" + id);
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        } else {
            return "redirect:/tasks"; // Lub strona błędu
        }
    }

    @PostMapping("/save") // Dla nowych zadań
    public String saveTask(@Valid @ModelAttribute("task") Task task, BindingResult result, Model model) {
        User currentUser = taskService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("priorities", priorities);
            model.addAttribute("statuses", statuses);
            model.addAttribute("formTitle", "Nowe zadanie");
            model.addAttribute("actionUrl", "/tasks/save");
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        }
        taskService.saveTask(task, currentUser);
        return "redirect:/tasks";
    }

    @PostMapping("/save/{id}") // Dla edytowanych zadań
    public String updateTask(@PathVariable("id") Long id, @Valid @ModelAttribute("task") Task task,
                             BindingResult result, Model model) {
        User currentUser = taskService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("priorities", priorities);
            model.addAttribute("statuses", statuses);
            model.addAttribute("formTitle", "Edytuj zadanie");
            model.addAttribute("actionUrl", "/tasks/save/" + id);
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        }
        // Upewniamy się, że edytujemy istniejące zadanie i należy ono do użytkownika
        Optional<Task> existingTaskOpt = taskService.findTaskByIdAndUser(id, currentUser);
        if(existingTaskOpt.isPresent()){
            Task existingTask = existingTaskOpt.get();
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDeadline(task.getDeadline());
            existingTask.setPriority(task.getPriority());
            existingTask.setStatus(task.getStatus());
            taskService.saveTask(existingTask, currentUser);
        } else {
            // Obsługa sytuacji, gdy zadanie nie istnieje lub nie należy do użytkownika
            return "redirect:/tasks?error=notFoundOrAccessDenied";
        }
        return "redirect:/tasks";
    }


    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        User currentUser = taskService.getCurrentUser();
        taskService.deleteTaskByIdAndUser(id, currentUser);
        return "redirect:/tasks";
    }
}