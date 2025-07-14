package org.backend.repository;

import org.backend.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerRepository extends JpaRepository<Box, Long> {
    List<Box> findByLockedTrue();
}