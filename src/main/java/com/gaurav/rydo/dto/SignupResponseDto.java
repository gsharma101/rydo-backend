package com.gaurav.rydo.dto;

import com.gaurav.rydo.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private Role role;

    private String message;
}