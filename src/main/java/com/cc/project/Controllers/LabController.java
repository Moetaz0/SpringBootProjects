package com.cc.project.Controllers;

import com.cc.project.Entity.Lab;
import com.cc.project.Service.LabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
public class LabController {
    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public List<Lab> getAllLabs() {
        return labService.getAllLabs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lab> getLabById(@PathVariable Long id) {
        return labService.getLabById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Lab createLab(@RequestBody Lab lab) {
        return labService.saveLab(lab);
    }

    @GetMapping("/specialty/{specialty}")
    public List<Lab> getBySpecialty(@PathVariable String specialty) {
        return labService.findBySpecialty(specialty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        labService.deleteLab(id);
        return ResponseEntity.noContent().build();
    }
}