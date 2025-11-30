package com.cc.project.Repository;

import com.cc.project.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByClientId(Long clientId);
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
