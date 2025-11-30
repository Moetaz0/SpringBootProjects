package com.cc.project.Controllers;

import com.cc.project.Entity.LabResult;
import com.cc.project.Service.LabResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
public class LabResultController {
    private final LabResultService labResultService;

    public LabResultController(LabResultService labResultService) {
        this.labResultService = labResultService;
    }

    @GetMapping
    public List<LabResult> getAllResults() {
        return labResultService.getAllResults();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResult> getResultById(@PathVariable Long id) {
        return labResultService.getResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LabResult sendResult(@RequestBody LabResult result) {
        return labResultService.saveResult(result);
    }

    @GetMapping("/client/{clientId}")
    public List<LabResult> getClientResults(@PathVariable Long clientId) {
        return labResultService.findByClient(clientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<LabResult> getDoctorResults(@PathVariable Long doctorId) {
        return labResultService.findByDoctor(doctorId);
    }

    @GetMapping("/lab/{labId}")
    public List<LabResult> getLabResults(@PathVariable Long labId) {
        return labResultService.findByLab(labId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        labResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}