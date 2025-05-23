package com.example.dashboardstudencki.repository;

import com.example.dashboardstudencki.model.Task;
import com.example.dashboardstudencki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserOrderByDeadlineAsc(User user);
    Optional<Task> findByIdAndUser(Long id, User user);

    @Query("SELECT t FROM Task t WHERE t.user = :user " +
            "AND ( (:searchTerm IS NULL OR :searchTerm = '') OR " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
            "(t.description LIKE CONCAT('%', :searchTerm, '%')) ) " +
            "AND ( (:status IS NULL OR :status = '') OR t.status = :status ) " +
            "AND ( (:priority IS NULL OR :priority = '') OR t.priority = :priority )")
    List<Task> searchTasksForUser(
            @Param("user") User user,
            @Param("searchTerm") String searchTerm,
            @Param("status") String status,
            @Param("priority") String priority,
            Sort sort
    );
}