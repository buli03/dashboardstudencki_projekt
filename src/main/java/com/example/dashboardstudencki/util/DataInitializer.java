package com.example.dashboardstudencki.util;

import com.example.dashboardstudencki.model.User;
import com.example.dashboardstudencki.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("student").isEmpty()) {
            User student = new User("student", passwordEncoder.encode("password123"), "ROLE_USER");
            userRepository.save(student);
            System.out.println(">>> Created default user: student / password123");
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN,ROLE_USER");
            userRepository.save(admin);
            System.out.println(">>> Created default user: admin / admin123");
        }
    }
}