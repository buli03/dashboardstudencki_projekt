package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.NoteRepository;
import com.example.dashboardstudencki.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    private final TaskRepository taskRepository;
    private final NoteRepository noteRepository;

    private static final String TASK_STATUS_COMPLETED = "Zakończone";
    private static final String TASK_STATUS_TODO = "Do zrobienia";
    private static final String TASK_STATUS_IN_PROGRESS = "W trakcie";


    @Autowired
    public StatisticsServiceImpl(TaskRepository taskRepository, NoteRepository noteRepository) {
        this.taskRepository = taskRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public long getTotalTasksCount(User user) {
        return taskRepository.countByUser(user);
    }

    @Override
    public long getCompletedTasksCount(User user) {
        return taskRepository.countByUserAndStatus(user, TASK_STATUS_COMPLETED);
    }

    @Override
    public long getToDoTasksCount(User user) {
        return taskRepository.countByUserAndStatus(user, TASK_STATUS_TODO);
    }

    @Override
    public long getInProgressTasksCount(User user) {
        return taskRepository.countByUserAndStatus(user, TASK_STATUS_IN_PROGRESS);
    }

    @Override
    public long getTotalNotesCount(User user) {
        return noteRepository.countByUser(user);
    }
}