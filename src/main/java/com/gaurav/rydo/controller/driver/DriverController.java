package com.gaurav.rydo.controller.driver;

import com.gaurav.rydo.dto.driver.DriverAvailabilityUpdateRequestDto;
import com.gaurav.rydo.dto.driver.DriverLocationUpdateRequestDto;
import com.gaurav.rydo.dto.driver.DriverRegistrationRequestDto;
import com.gaurav.rydo.dto.driver.DriverResponseDto;
import com.gaurav.rydo.service.driver.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/register")
    public ResponseEntity<DriverResponseDto> registerDriver(
            @Valid @RequestBody DriverRegistrationRequestDto requestDto
    ) {

        DriverResponseDto response =
                driverService.registerDriver(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/me")
    public ResponseEntity<DriverResponseDto> getCurrentDriver() {

        DriverResponseDto response =
                driverService.getCurrentDriver();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/availability")
    public ResponseEntity<DriverResponseDto> updateAvailability(
            @Valid @RequestBody
            DriverAvailabilityUpdateRequestDto requestDto
    ) {

        DriverResponseDto response =
                driverService.updateAvailability(requestDto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/location")
    public ResponseEntity<DriverResponseDto> updateLocation(
            @Valid @RequestBody
            DriverLocationUpdateRequestDto requestDto
    ) {

        DriverResponseDto response =
                driverService.updateLocation(requestDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<DriverResponseDto>> findNearbyDrivers(

            @RequestParam Double latitude,

            @RequestParam Double longitude,

            @RequestParam Double radius
    ) {

        List<DriverResponseDto> response =
                driverService.findNearbyDrivers(
                        latitude,
                        longitude,
                        radius
                );

        return ResponseEntity.ok(response);
    }
}