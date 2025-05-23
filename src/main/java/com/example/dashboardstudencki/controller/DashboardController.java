package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.model.User; // Dodaj import
import com.example.dashboardstudencki.repository.UserRepository; // Dodaj import
import com.example.dashboardstudencki.service.StatisticsService; // Dodaj import
import org.springframework.beans.factory.annotation.Autowired; // Dodaj import
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Dodaj import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final StatisticsService statisticsService;
    private final UserRepository userRepository; // Do pobrania obiektu User

    @Autowired
    public DashboardController(StatisticsService statisticsService, UserRepository userRepository) {
        this.statisticsService = statisticsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, Authentication authentication) {
        String username = null;
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);

            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username"));

            model.addAttribute("totalTasks", statisticsService.getTotalTasksCount(currentUser));
            model.addAttribute("completedTasks", statisticsService.getCompletedTasksCount(currentUser));
            model.addAttribute("todoTasks", statisticsService.getToDoTasksCount(currentUser));
            model.addAttribute("inProgressTasks", statisticsService.getInProgressTasksCount(currentUser));
            model.addAttribute("totalNotes", statisticsService.getTotalNotesCount(currentUser));
        }
        model.addAttribute("activePage", "dashboard");
        return "dashboard/index";
    }
}