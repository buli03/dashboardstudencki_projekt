package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.User;

public interface StatisticsService {
    long getTotalTasksCount(User user);
    long getCompletedTasksCount(User user);
    long getToDoTasksCount(User user);
    long getInProgressTasksCount(User user);
    long getTotalNotesCount(User user);
}