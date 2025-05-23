package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.dto.UserRegistrationDto;
import com.example.dashboardstudencki.exception.PasswordMismatchException;
import com.example.dashboardstudencki.exception.UserAlreadyExistException;
import com.example.dashboardstudencki.model.User;

public interface UserService {
    void registerNewUser(UserRegistrationDto registrationDto) throws UserAlreadyExistException, PasswordMismatchException;
    User findByUsername(String username);
    User getCurrentAuthenticatedUser();
}