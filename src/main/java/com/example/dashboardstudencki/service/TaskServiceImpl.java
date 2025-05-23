package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.TaskRepository;
import com.example.dashboardstudencki.repository.UserRepository; // Dodaj import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Dodaj import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Dodaj import

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Dobra praktyka dla metod serwisowych modyfikujących dane
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository; // Do pobierania aktualnego użytkownika

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true) // Tylko odczyt
    public List<Task> findAllTasksByUser(User user) {
        return taskRepository.findByUserOrderByDeadlineAsc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findTaskByIdAndUser(Long id, User user) {
        return taskRepository.findByIdAndUser(id, user);
    }

    @Override
    public Task saveTask(Task task, User user) {
        // Upewniamy się, że zadanie jest powiązane z prawidłowym użytkownikiem
        // To jest dodatkowe zabezpieczenie, jeśli user nie jest ustawiany w kontrolerze
        if (task.getUser() == null || !task.getUser().getId().equals(user.getId())) {
            task.setUser(user);
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTaskByIdAndUser(Long id, User user) {
        // Sprawdzamy, czy zadanie należy do użytkownika przed usunięciem
        Optional<Task> taskOptional = taskRepository.findByIdAndUser(id, user);
        taskOptional.ifPresent(taskRepository::delete);
        // Możesz dodać obsługę, jeśli zadanie nie zostanie znalezione lub nie należy do użytkownika
    }

    // Pomocnicza metoda do pobrania aktualnie zalogowanego użytkownika (można ją przenieść do UserService)
    // Jeśli masz już UserService z taką metodą, użyj jej.
    // Na potrzeby TaskService, możemy też zaimplementować ją lokalnie lub wstrzyknąć UserRepository
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));
    }
}