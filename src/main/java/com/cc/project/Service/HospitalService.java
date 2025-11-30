package com.cc.project.Service;

import com.cc.project.Entity.Hospital;
import com.cc.project.Repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Optional<Hospital> getHospitalById(Long id) {
        return hospitalRepository.findById(id);
    }

    public Hospital saveHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public void deleteHospital(Long id) {
        hospitalRepository.deleteById(id);
    }

    public List<Hospital> findNearbyHospitals(double minLat, double maxLat, double minLng, double maxLng) {
        return hospitalRepository.findNearbyHospitals(minLat, maxLat, minLng, maxLng);
    }
}