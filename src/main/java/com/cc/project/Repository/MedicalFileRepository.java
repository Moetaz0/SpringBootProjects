package com.cc.project.Repository;

import com.cc.project.Entity.MedicalFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalFileRepository extends JpaRepository<MedicalFile, Long> {
    List<MedicalFile> findByUploadedById(Long userId);
    List<MedicalFile> findByAppointmentId(Long appointmentId);
}