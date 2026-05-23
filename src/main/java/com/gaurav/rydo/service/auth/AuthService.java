package com.gaurav.rydo.service.auth;

import com.gaurav.rydo.dto.LoginRequestDto;
import com.gaurav.rydo.dto.LoginResponseDto;
import com.gaurav.rydo.dto.auth.SignupRequestDto;
import com.gaurav.rydo.dto.auth.SignupResponseDto;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.repository.user.UserRepository;
import com.gaurav.rydo.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        // Find user by email
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        boolean isPasswordMatched = passwordEncoder.matches(
                loginRequestDto.getPassword(),
                user.getPassword()
        );

        if (!isPasswordMatched) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail());

        // Return response
        return LoginResponseDto.builder()
                .token(token)
                .message("Login successful")
                .build();
    }
}