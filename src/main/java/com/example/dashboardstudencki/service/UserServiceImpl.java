package com.example.dashboardstudencki.service;

import com.example.dashboardstudencki.dto.UserRegistrationDto;
import com.example.dashboardstudencki.exception.PasswordMismatchException;
import com.example.dashboardstudencki.exception.UserAlreadyExistException;
import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerNewUser(UserRegistrationDto registrationDto) throws UserAlreadyExistException, PasswordMismatchException {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Użytkownik o nazwie " + registrationDto.getUsername() + " już istnieje.");
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new PasswordMismatchException("Hasła nie są zgodne.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setRoles("ROLE_USER");

        userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("Nie znaleziono zalogowanego użytkownika lub użytkownik jest anonimowy.");
        }
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o nazwie: " + currentPrincipalName));
    }
}