package com.gaurav.rydo.service;

import com.gaurav.rydo.dto.SignupRequestDto;
import com.gaurav.rydo.dto.SignupResponseDto;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(signupRequestDto.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Create User entity
        User user = User.builder()
                .firstName(signupRequestDto.getFirstName())
                .lastName(signupRequestDto.getLastName())
                .email(signupRequestDto.getEmail())
                .phoneNumber(signupRequestDto.getPhoneNumber())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(signupRequestDto.getRole())
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Return response
        return SignupResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }
}