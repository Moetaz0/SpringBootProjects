package com.cc.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cc.project.Entity.Prescription;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Patient view
    List<Prescription> findByPatientId(Long patientId);

    // Doctor view (prescriptions he created)
    List<Prescription> findByDoctorId(Long doctorId);
}
