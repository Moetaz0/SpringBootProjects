package com.cc.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cc.project.Entity.RefillRequest;
import java.util.List;

@Repository
public interface RefillRequestRepository extends JpaRepository<RefillRequest, Long> {

    List<RefillRequest> findByPatientId(Long patientId);

    List<RefillRequest> findByPrescriptionDoctorId(Long doctorId);
}
