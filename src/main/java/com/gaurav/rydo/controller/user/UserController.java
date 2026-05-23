package com.gaurav.rydo.controller.user;

import com.gaurav.rydo.dto.user.UserProfileResponseDto;
import com.gaurav.rydo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponseDto getCurrentUser() {

        return userService.getCurrentUser();
    }
}