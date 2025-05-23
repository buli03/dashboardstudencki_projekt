package com.example.dashboardstudencki.controller;

import com.example.dashboardstudencki.dto.UserRegistrationDto;
import com.example.dashboardstudencki.exception.PasswordMismatchException;
import com.example.dashboardstudencki.exception.UserAlreadyExistException;
import com.example.dashboardstudencki.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userDto")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("activePage", "register");
        return "register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("userDto") @Valid UserRegistrationDto registrationDto,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "register");
            return "register";
        }

        try {
            userService.registerNewUser(registrationDto);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("username", "user.exists", e.getMessage());
            model.addAttribute("activePage", "register");
            return "register";
        } catch (PasswordMismatchException e) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", e.getMessage());
            model.addAttribute("activePage", "register");
            return "register";
        }

        redirectAttributes.addFlashAttribute("registrationSuccess", "Rejestracja zakończona pomyślnie! Możesz się teraz zalogować.");
        return "redirect:/login";
    }
}