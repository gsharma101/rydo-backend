package com.gaurav.rydo.controller.ride;

import com.gaurav.rydo.dto.ride.RideHistoryResponseDto;
import com.gaurav.rydo.dto.ride.RideRequestDto;
import com.gaurav.rydo.dto.ride.RideResponseDto;
import com.gaurav.rydo.service.ride.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(

            @Valid @RequestBody
            RideRequestDto requestDto
    ) {

        RideResponseDto response =
                rideService.requestRide(requestDto);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{rideId}/accept")
    public ResponseEntity<RideResponseDto> acceptRide(
            @PathVariable Long rideId
    ) {

        RideResponseDto response =
                rideService.acceptRide(rideId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/start")
    public ResponseEntity<RideResponseDto> startRide(
            @PathVariable Long rideId
    ) {

        RideResponseDto response =
                rideService.startRide(rideId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/complete")
    public ResponseEntity<RideResponseDto> completeRide(
            @PathVariable Long rideId
    ) {

        RideResponseDto response =
                rideService.completeRide(rideId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-rides")
    public ResponseEntity<List<RideHistoryResponseDto>> getMyRides() {

        return ResponseEntity.ok(
                rideService.getMyRides()
        );
    }

    @GetMapping("/driver-rides")
    public ResponseEntity<List<RideHistoryResponseDto>> getDriverRides() {

        return ResponseEntity.ok(
                rideService.getDriverRides()
        );
    }
}