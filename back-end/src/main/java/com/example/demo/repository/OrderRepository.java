package com.example.demo.repository;

import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findById(Long id);
}
