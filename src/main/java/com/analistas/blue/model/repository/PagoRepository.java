package com.analistas.blue.model.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.analistas.blue.model.domain.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    boolean existsByPaymentId(String paymentId);

}
