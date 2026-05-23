package com.gaurav.rydo.controller.auth;

import com.gaurav.rydo.dto.LoginRequestDto;
import com.gaurav.rydo.dto.LoginResponseDto;
import com.gaurav.rydo.dto.auth.SignupRequestDto;
import com.gaurav.rydo.dto.auth.SignupResponseDto;
import com.gaurav.rydo.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto
    ) {

        SignupResponseDto response = authService.signup(signupRequestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {

        LoginResponseDto response = authService.login(loginRequestDto);

        return ResponseEntity.ok(response);
    }
}