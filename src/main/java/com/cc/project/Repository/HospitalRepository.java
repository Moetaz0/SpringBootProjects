package com.cc.project.Repository;

import com.cc.project.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h WHERE h.latitude BETWEEN ?1 AND ?2 AND h.longitude BETWEEN ?3 AND ?4")
    List<Hospital> findNearbyHospitals(double minLat, double maxLat, double minLng, double maxLng);
}