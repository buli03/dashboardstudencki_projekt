package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAllTasksByUser(User user);
    Optional<Task> findTaskByIdAndUser(Long id, User user);
    Task saveTask(Task task, User user);
    void deleteTaskByIdAndUser(Long id, User user);

    List<Task> searchAndSortTasks(User user, String searchTerm, String status, String priority, String sortBy, String sortDirection);
}