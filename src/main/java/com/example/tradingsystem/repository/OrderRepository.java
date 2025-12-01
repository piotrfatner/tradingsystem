package com.example.tradingsystem.repository;

import com.example.tradingsystem.entity.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<TradeOrder, Long> {
    TradeOrder findByOrderId(Long orderId);
}
