package com.example.demo.repository;

import com.example.demo.entity.UserAddress;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser(Users user);

    boolean existsByUserAndIsDefaultTrue(Users user);

    Optional<UserAddress> findByUserAndIsDefaultTrue(Users user);

    Optional<UserAddress> findByUserAndIsDefault(Users user, Boolean isDefault);
}