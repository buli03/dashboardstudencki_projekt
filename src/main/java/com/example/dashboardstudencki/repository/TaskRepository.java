package com.example.dashboardstudencki.repository;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Znajdź wszystkie zadania dla danego użytkownika
    List<Task> findByUserOrderByDeadlineAsc(User user);

    // Znajdź zadanie po ID i użytkowniku (dla bezpieczeństwa, aby użytkownik nie edytował cudzych zadań)
    Optional<Task> findByIdAndUser(Long id, User user);
}