package com.cc.project.Repository;

import com.cc.project.Entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByClientId(Long clientId);
    List<LabResult> findByDoctorId(Long doctorId);
    List<LabResult> findByLabId(Long labId);
}