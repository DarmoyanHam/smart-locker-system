package org.backend.repository;

import org.backend.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoxRepository extends JpaRepository<Box, Long> {
    List<Box> findByIsEmptyFalse();
    Optional<Box> findById(Long id);

}