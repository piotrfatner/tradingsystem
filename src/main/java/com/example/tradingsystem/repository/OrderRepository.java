package com.example.tradingsystem.repository;

import com.example.tradingsystem.entity.TradeOrder;
import com.example.tradingsystem.enumes.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<TradeOrder, Long> {
    Optional<TradeOrder> findByOrderId(Long orderId);

    List<TradeOrder> findByStatus(OrderStatus status);
}
