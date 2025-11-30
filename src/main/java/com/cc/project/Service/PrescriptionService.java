package com.cc.project.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import com.cc.project.Entity.*;
import com.cc.project.Repository.*;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final RefillRequestRepository refillRequestRepository;
    private final UserRepository userRepository;

    // --------------------------
    // PATIENT'S PRESCRIPTIONS
    // --------------------------
    public List<Prescription> getPatientPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    // --------------------------
    // DOCTOR: Prescriptions he created
    // --------------------------
    public List<Prescription> getDoctorPrescriptions(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    // --------------------------
    // DOCTOR: Create Prescription
    // --------------------------
    public Prescription createPrescription(Long doctorId, Long patientId, Prescription pres) {

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        pres.setDoctor(doctor);
        pres.setPatient(patient);
        pres.setStatus(PrescriptionStatus.APPROVED);

        return prescriptionRepository.save(pres);
    }

    // --------------------------
    // PATIENT: Request refill
    // --------------------------
    public RefillRequest requestRefill(Long presId, Long patientId) {

        Prescription pres = prescriptionRepository.findById(presId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (!pres.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Not allowed.");
        }

        RefillRequest req = new RefillRequest();
        req.setPrescription(pres);
        req.setPatient(pres.getPatient());

        return refillRequestRepository.save(req);
    }

    // --------------------------
    // DOCTOR: Approve/Deny refill
    // --------------------------
    public RefillRequest updateRefill(Long refillId, boolean approve, Long doctorId) {

        RefillRequest req = refillRequestRepository.findById(refillId)
                .orElseThrow(() -> new RuntimeException("Refill request not found"));

        Long doctorOfPrescription = req.getPrescription().getDoctor().getId();

        if (!doctorOfPrescription.equals(doctorId)) {
            throw new RuntimeException("Unauthorized");
        }

        req.setStatus(approve ? RefillRequest.RefillStatus.APPROVED : RefillRequest.RefillStatus.DENIED);

        return refillRequestRepository.save(req);
    }
}
