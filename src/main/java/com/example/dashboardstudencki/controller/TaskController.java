package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.service.TaskServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;
    private final List<String> ALL_PRIORITIES = Arrays.asList("Niski", "Średni", "Wysoki");
    private final List<String> ALL_STATUSES = Arrays.asList("Do zrobienia", "W trakcie", "Zakończone");


    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model,
                            @RequestParam(value = "searchTerm", required = false) String searchTerm,
                            @RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "priority", required = false) String priority,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "deadline") String sortBy,
                            @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        User currentUser = taskService.getCurrentUser();
        List<Task> tasks = taskService.searchAndSortTasks(currentUser, searchTerm, status, priority, sortBy, sortDirection);

        model.addAttribute("tasks", tasks);
        model.addAttribute("activePage", "tasks");

        // Przekazanie parametrów wyszukiwania/filtrowania/sortowania z powrotem do widoku
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);

        // Przekazanie list dostępnych opcji dla filtrów
        model.addAttribute("priorities", ALL_PRIORITIES);
        model.addAttribute("statuses", ALL_STATUSES);

        return "tasks/list";
    }

    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", ALL_PRIORITIES);
        model.addAttribute("statuses", ALL_STATUSES);
        model.addAttribute("formTitle", "Nowe zadanie");
        model.addAttribute("actionUrl", "/tasks/save");
        model.addAttribute("activePage", "tasks");
        return "tasks/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        User currentUser = taskService.getCurrentUser();
        Optional<Task> taskOptional = taskService.findTaskByIdAndUser(id, currentUser);
        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("priorities", ALL_PRIORITIES);
            model.addAttribute("statuses", ALL_STATUSES);
            model.addAttribute("formTitle", "Edytuj zadanie");
            model.addAttribute("actionUrl", "/tasks/save/" + id);
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        } else {
            return "redirect:/tasks?error=notFoundOrAccessDenied";
        }
    }

    @PostMapping("/save")
    public String saveTask(@Valid @ModelAttribute("task") Task task, BindingResult result, Model model) {
        User currentUser = taskService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("priorities", ALL_PRIORITIES);
            model.addAttribute("statuses", ALL_STATUSES);
            model.addAttribute("formTitle", "Nowe zadanie");
            model.addAttribute("actionUrl", "/tasks/save");
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        }
        taskService.saveTask(task, currentUser);
        return "redirect:/tasks";
    }

    @PostMapping("/save/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid @ModelAttribute("task") Task task,
                             BindingResult result, Model model) {
        User currentUser = taskService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("priorities", ALL_PRIORITIES);
            model.addAttribute("statuses", ALL_STATUSES);
            model.addAttribute("formTitle", "Edytuj zadanie");
            model.addAttribute("actionUrl", "/tasks/save/" + id);
            model.addAttribute("activePage", "tasks");
            return "tasks/form";
        }
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
            return "redirect:/tasks?error=notFoundOrAccessDeniedDuringUpdate";
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