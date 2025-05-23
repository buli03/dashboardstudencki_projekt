package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.TaskRepository;
import com.example.dashboardstudencki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
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
        if (task.getUser() == null || !task.getUser().getId().equals(user.getId())) {
            task.setUser(user);
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTaskByIdAndUser(Long id, User user) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUser(id, user);
        taskOptional.ifPresent(taskRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> searchAndSortTasks(User user, String searchTerm, String status, String priority, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.ASC, "deadline"); // Domyślnie po terminie rosnąco

        if (StringUtils.hasText(sortBy) && StringUtils.hasText(sortDirection)) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            sort = Sort.by(direction, sortBy);
        } else if (StringUtils.hasText(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }

        String normalizedSearchTerm = StringUtils.hasText(searchTerm) ? searchTerm : null;
        String normalizedStatus = StringUtils.hasText(status) ? status : null;
        String normalizedPriority = StringUtils.hasText(priority) ? priority : null;

        return taskRepository.searchTasksForUser(user, normalizedSearchTerm, normalizedStatus, normalizedPriority, sort);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));
    }
}