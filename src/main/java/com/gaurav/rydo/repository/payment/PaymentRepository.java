package com.gaurav.rydo.repository.payment;

import com.gaurav.rydo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
}