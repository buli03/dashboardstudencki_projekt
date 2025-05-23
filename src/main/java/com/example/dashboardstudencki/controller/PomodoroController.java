package com.example.dashboardstudencki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Dodaj import dla Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pomodoro")
public class PomodoroController {

    @GetMapping
    public String pomodoroPage(Model model) {
        model.addAttribute("activePage", "pomodoro");
        return "pomodoro/timer";
    }
}