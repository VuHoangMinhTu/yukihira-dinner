package com.example.demo.repository;

import com.example.demo.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findById(Long id);
}
