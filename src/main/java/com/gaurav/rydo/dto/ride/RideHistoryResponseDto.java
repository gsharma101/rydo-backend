package com.gaurav.rydo.dto.ride;

import com.gaurav.rydo.entity.enums.RideStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RideHistoryResponseDto {

    private Long rideId;

    private String riderName;

    private String driverName;

    private Double fare;

    private RideStatus status;

    private LocalDateTime requestedAt;
}