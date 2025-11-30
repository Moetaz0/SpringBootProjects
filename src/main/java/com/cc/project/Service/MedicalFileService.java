package com.cc.project.Service;

import com.cc.project.Entity.MedicalFile;
import com.cc.project.Repository.MedicalFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalFileService {
    private final MedicalFileRepository medicalFileRepository;

    public MedicalFileService(MedicalFileRepository medicalFileRepository) {
        this.medicalFileRepository = medicalFileRepository;
    }

    public List<MedicalFile> getAllFiles() {
        return medicalFileRepository.findAll();
    }

    public Optional<MedicalFile> getFileById(Long id) {
        return medicalFileRepository.findById(id);
    }

    public MedicalFile saveFile(MedicalFile file) {
        return medicalFileRepository.save(file);
    }

    public void deleteFile(Long id) {
        medicalFileRepository.deleteById(id);
    }

    public List<MedicalFile> findByUser(Long userId) {
        return medicalFileRepository.findByUploadedById(userId);
    }

    public List<MedicalFile> findByAppointment(Long appointmentId) {
        return medicalFileRepository.findByAppointmentId(appointmentId);
    }
}