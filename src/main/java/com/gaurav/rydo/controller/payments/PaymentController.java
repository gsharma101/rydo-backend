package com.gaurav.rydo.controller.payments;

import com.gaurav.rydo.dto.payment.PaymentRequestDto;
import com.gaurav.rydo.dto.payment.PaymentResponseDto;
import com.gaurav.rydo.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('RIDER')")
    @PostMapping("/{rideId}")
    public ResponseEntity<PaymentResponseDto> makePayment(

            @PathVariable Long rideId,

            @RequestBody
            PaymentRequestDto requestDto
    ) {

        PaymentResponseDto response =
                paymentService.makePayment(
                        rideId,
                        requestDto
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }
}