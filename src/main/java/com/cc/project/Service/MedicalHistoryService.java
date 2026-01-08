package com.cc.project.Service;

import com.cc.project.Entity.MedicalHistory;
import com.cc.project.Entity.User;
import com.cc.project.Repository.MedicalHistoryRepository;
import com.cc.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final UserRepository userRepository;

    // Create or Update
    public MedicalHistory save(Long userId, MedicalHistory mh) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Force the medical history to attach to the correct user
        mh.setUser(user);

        // Keep the same ID if updating
        Optional<MedicalHistory> existing = medicalHistoryRepository.findByUserId(userId);
        existing.ifPresent(value -> mh.setId(value.getId()));

        return medicalHistoryRepository.save(mh);
    }

    public Optional<MedicalHistory> getByUserId(Long userId) {
        return medicalHistoryRepository.findByUserId(userId);
    }

    public void delete(Long id) {
        medicalHistoryRepository.deleteById(id);
    }
}
