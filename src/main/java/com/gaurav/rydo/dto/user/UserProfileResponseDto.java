package com.gaurav.rydo.dto.user;

import com.gaurav.rydo.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;
}