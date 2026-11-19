package com.example.hospital_manager.config;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfMissing("admin", "System Administrator", "admin123", Role.ADMIN);
        createUserIfMissing("doctor", "Dr. Nimal Perera", "doctor123", Role.DOCTOR);
        createUserIfMissing("nurse", "Nurse Kavindi Silva", "nurse123", Role.NURSE);
        createUserIfMissing("receptionist", "Kamal Bandara", "recep123", Role.RECEPTIONIST);

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .fullName("Saman Kumara")
                    .dateOfBirth(LocalDate.of(1992, 5, 14))
                    .gender("Male")
                    .phone("0771234567")
                    .address("Kelaniya, Sri Lanka")
                    .build());

            patientRepository.save(Patient.builder()
                    .fullName("Nadeesha Fernando")
                    .dateOfBirth(LocalDate.of(1998, 9, 22))
                    .gender("Female")
                    .phone("0719876543")
                    .address("Colombo, Sri Lanka")
                    .build());
        }
    }

    private void createUserIfMissing(String username, String fullName, String rawPassword, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername(username);
            user.setFullName(fullName);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            userRepository.save(user);
        }
    }
}