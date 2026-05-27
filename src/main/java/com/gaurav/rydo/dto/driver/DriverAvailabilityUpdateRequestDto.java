package com.gaurav.rydo.dto.driver;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverAvailabilityUpdateRequestDto {

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;
}