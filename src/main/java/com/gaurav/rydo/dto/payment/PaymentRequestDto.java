package com.gaurav.rydo.dto.payment;

import com.gaurav.rydo.entity.enums.PaymentMethod;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private PaymentMethod paymentMethod;
}