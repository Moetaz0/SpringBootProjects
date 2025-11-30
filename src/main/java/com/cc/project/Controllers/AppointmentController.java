package com.cc.project.Controllers;

import com.cc.project.Entity.Appointment;
import com.cc.project.Service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus(Appointment.Status.PENDING);

        return appointmentService.saveAppointment(appointment);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.findByDoctor(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public List<Appointment> getDoctorAppointmentsByDate(@PathVariable Long doctorId, @PathVariable String date) {
        return appointmentService.findByDoctorAndDate(doctorId, LocalDate.parse(date));
    }

    @GetMapping("/client/{clientId}")
    public List<Appointment> getClientAppointments(@PathVariable Long clientId) {
        return appointmentService.findByClient(clientId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}