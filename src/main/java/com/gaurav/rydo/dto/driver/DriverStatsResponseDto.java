package com.gaurav.rydo.dto.driver;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverStatsResponseDto {

    private Long totalRides;

    private Long completedRides;

    private Long cancelledRides;

    private Double averageRating;

    private Double totalEarnings;
}