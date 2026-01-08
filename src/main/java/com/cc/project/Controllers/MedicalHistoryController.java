package com.cc.project.Controllers;

import com.cc.project.Entity.MedicalHistory;
import com.cc.project.Service.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getHistoryByUser(@PathVariable Long userId) {
        return medicalHistoryService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<MedicalHistory> saveOrUpdate(
            @PathVariable Long userId,
            @RequestBody MedicalHistory medicalHistory) {
        MedicalHistory saved = medicalHistoryService.save(userId, medicalHistory);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        medicalHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
