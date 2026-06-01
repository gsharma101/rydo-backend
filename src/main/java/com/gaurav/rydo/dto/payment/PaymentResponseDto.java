package com.gaurav.rydo.dto.payment;

import com.gaurav.rydo.entity.enums.PaymentMethod;
import com.gaurav.rydo.entity.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;

    private Long rideId;

    private Double amount;

    private PaymentStatus status;

    private PaymentMethod paymentMethod;

    private LocalDateTime paidAt;
}