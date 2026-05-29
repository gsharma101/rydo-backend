package com.gaurav.rydo.dto.ride;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RateRideRequestDto {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}