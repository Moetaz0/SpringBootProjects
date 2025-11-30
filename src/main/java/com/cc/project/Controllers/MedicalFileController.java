package com.cc.project.Controllers;

import com.cc.project.Entity.MedicalFile;
import com.cc.project.Service.MedicalFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class MedicalFileController {
    private final MedicalFileService medicalFileService;

    public MedicalFileController(MedicalFileService medicalFileService) {
        this.medicalFileService = medicalFileService;
    }

    @GetMapping
    public List<MedicalFile> getAllFiles() {
        return medicalFileService.getAllFiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalFile> getFileById(@PathVariable Long id) {
        return medicalFileService.getFileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<MedicalFile> getFilesByUser(@PathVariable Long userId) {
        return medicalFileService.findByUser(userId);
    }

    @GetMapping("/appointment/{appointmentId}")
    public List<MedicalFile> getFilesByAppointment(@PathVariable Long appointmentId) {
        return medicalFileService.findByAppointment(appointmentId);
    }

    @PostMapping
    public MedicalFile uploadFile(@RequestBody MedicalFile file) {
        return medicalFileService.saveFile(file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        medicalFileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}