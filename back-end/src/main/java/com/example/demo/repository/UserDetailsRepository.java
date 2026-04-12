package com.example.demo.repository;

import com.example.demo.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetails> findByUsers_UserId(Long aLong);
}