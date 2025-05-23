package com.example.dashboardstudencki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        // Jeśli użytkownik jest już zalogowany, możesz go przekierować na dashboard
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        // return "redirect:/dashboard";
        // }
        return "login"; // Nazwa pliku login.html w templates
    }
}