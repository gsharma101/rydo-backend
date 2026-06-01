package com.gaurav.rydo.repository.payment;

import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
                SELECT COALESCE(SUM(p.amount), 0)
                FROM Payment p
                WHERE p.ride.driver = :driver
                AND p.status = 'SUCCESS'
            """)
    Double getTotalEarningsByDriver(
            @Param("driver") Driver driver
    );
}