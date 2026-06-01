package com.gaurav.rydo.service.payment;

import com.gaurav.rydo.dto.payment.PaymentRequestDto;
import com.gaurav.rydo.dto.payment.PaymentResponseDto;
import com.gaurav.rydo.entity.Payment;
import com.gaurav.rydo.entity.Ride;
import com.gaurav.rydo.entity.enums.PaymentStatus;
import com.gaurav.rydo.entity.enums.RideStatus;
import com.gaurav.rydo.exception.ApiException;
import com.gaurav.rydo.repository.payment.PaymentRepository;
import com.gaurav.rydo.repository.ride.RideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final RideRepository rideRepository;

    @Transactional
    public PaymentResponseDto makePayment(
            Long rideId,
            PaymentRequestDto requestDto
    ) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new ApiException("Ride not found"));

        if (ride.getStatus() != RideStatus.COMPLETED) {

            throw new ApiException(
                    "Payment can only be made for completed rides"
            );
        }

        Payment payment = Payment.builder()
                .ride(ride)
                .amount(ride.getFare())
                .status(PaymentStatus.SUCCESS)
                .method(requestDto.getPaymentMethod())
                .paidAt(LocalDateTime.now())
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        return PaymentResponseDto.builder()
                .paymentId(savedPayment.getId())
                .rideId(ride.getId())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus())
                .paymentMethod(savedPayment.getMethod())
                .paidAt(savedPayment.getPaidAt())
                .build();
    }
}