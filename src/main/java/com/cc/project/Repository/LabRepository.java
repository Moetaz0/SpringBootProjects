package com.cc.project.Repository;

import com.cc.project.Entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {
    List<Lab> findBySpecialty(String specialty);
}