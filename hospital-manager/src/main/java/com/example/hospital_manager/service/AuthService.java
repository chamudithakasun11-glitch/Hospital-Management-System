package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AuthResponse;
import com.example.hospital_manager.dto.LoginRequest;
import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    
    public AppUser register(RegisterRequest request) {
        
        
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        
        AppUser user = new AppUser();
        user.setUsername(request.username());
        
        
        user.setPassword(passwordEncoder.encode(request.password()));
        
        
        user.setRole(Role.valueOf(request.role().toUpperCase()));

        
        return userRepository.save(user);
    }

    
    public AuthResponse login(LoginRequest request) {
        
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        
        String jwtToken = jwtService.generateToken(userDetails.getUsername(), user.getRole().name());

        
        return new AuthResponse(
                jwtToken,
                "Bearer",
                3600000,
                user.getUsername(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}