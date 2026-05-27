package com.gaurav.rydo.dto.ride;

import com.gaurav.rydo.entity.enums.RideStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RideResponseDto {

    private Long rideId;

    private String riderName;

    private String driverName;

    private String vehicleNumber;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    private Double fare;

    private RideStatus status;

    private LocalDateTime requestedAt;
}