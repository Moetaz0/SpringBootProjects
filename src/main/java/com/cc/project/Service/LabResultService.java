package com.cc.project.Service;

import com.cc.project.Entity.LabResult;
import com.cc.project.Repository.LabResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabResultService {
    private final LabResultRepository labResultRepository;

    public LabResultService(LabResultRepository labResultRepository) {
        this.labResultRepository = labResultRepository;
    }

    public List<LabResult> getAllResults() {
        return labResultRepository.findAll();
    }

    public Optional<LabResult> getResultById(Long id) {
        return labResultRepository.findById(id);
    }

    public LabResult saveResult(LabResult result) {
        return labResultRepository.save(result);
    }

    public void deleteResult(Long id) {
        labResultRepository.deleteById(id);
    }

    public List<LabResult> findByClient(Long clientId) {
        return labResultRepository.findByClientId(clientId);
    }

    public List<LabResult> findByDoctor(Long doctorId) {
        return labResultRepository.findByDoctorId(doctorId);
    }

    public List<LabResult> findByLab(Long labId) {
        return labResultRepository.findByLabId(labId);
    }
}

