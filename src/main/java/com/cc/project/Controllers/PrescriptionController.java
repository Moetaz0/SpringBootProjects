package com.cc.project.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.cc.project.Entity.*;
import com.cc.project.Service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // --------------------------
    // PATIENT: Get own prescriptions
    // --------------------------
    @GetMapping("/patient/{patientId}")
    public List<Prescription> getPatientPrescriptions(@PathVariable Long patientId) {
        return prescriptionService.getPatientPrescriptions(patientId);
    }

    // --------------------------
    // DOCTOR: Get all prescriptions the doctor wrote
    // --------------------------
    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return prescriptionService.getDoctorPrescriptions(doctorId);
    }

    // --------------------------
    // DOCTOR: Create prescription
    // --------------------------
    @PostMapping("/create/{doctorId}/{patientId}")
    public Prescription createPrescription(
            @PathVariable Long doctorId,
            @PathVariable Long patientId,
            @RequestBody Prescription prescription) {

        return prescriptionService.createPrescription(doctorId, patientId, prescription);
    }

    // --------------------------
    // PATIENT: Request refill
    // --------------------------
    @PostMapping("/refill/{prescriptionId}/{patientId}")
    public RefillRequest requestRefill(
            @PathVariable Long prescriptionId,
            @PathVariable Long patientId) {

        return prescriptionService.requestRefill(prescriptionId, patientId);
    }

    // --------------------------
    // DOCTOR: Approve or Deny refill
    // --------------------------
    @PutMapping("/refill/update/{refillId}/{doctorId}")
    public RefillRequest updateRefill(
            @PathVariable Long refillId,
            @PathVariable Long doctorId,
            @RequestParam boolean approve) {

        return prescriptionService.updateRefill(refillId, approve, doctorId);
    }
}
