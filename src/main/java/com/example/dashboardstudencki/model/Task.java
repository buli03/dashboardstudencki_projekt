package com.example.dashboardstudencki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob // Dla dłuższych opisów
    private String description;

    private LocalDateTime deadline;

    @Column(nullable = false)
    private String priority; // Np. "Wysoki", "Średni", "Niski"

    @Column(nullable = false)
    private String status; // Np. "Do zrobienia", "W trakcie", "Zakończone"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relacja do użytkownika

    // Konstruktor dla łatwiejszego tworzenia
    public Task(String title, String description, LocalDateTime deadline, String priority, String status, User user) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}