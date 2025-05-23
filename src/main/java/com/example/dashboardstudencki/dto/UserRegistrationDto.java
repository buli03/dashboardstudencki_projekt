package com.example.dashboardstudencki.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 3, max = 20, message = "Nazwa użytkownika musi mieć od 3 do 20 znaków")
    private String username;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 6, max = 100, message = "Hasło musi mieć od 6 do 100 znaków")
    private String password;

    @NotBlank(message = "Potwierdzenie hasła jest wymagane")
    private String confirmPassword;
}