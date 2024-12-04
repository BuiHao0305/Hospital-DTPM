package com.springpro.hospital.Services;

import com.springpro.hospital.Entities.Appointment;
import com.springpro.hospital.Repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment addAppointment(Appointment appointment) {
        appointment.setStatus(false); // Đặt giá trị mặc định là false
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(String id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Appointment not found"));

        appointment.setPatientName(appointmentDetails.getPatientName());
        appointment.setPhoneNumber(appointmentDetails.getPhoneNumber());
        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setNotes(appointmentDetails.getNotes());

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }

    // Thêm phương thức để cập nhật status thành true
    public Appointment updateStatusToTrue(String id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Appointment not found"));

        appointment.setStatus(true); // Cập nhật status thành true
        return appointmentRepository.save(appointment); // Lưu thay đổi vào cơ sở dữ liệu
    }
    public List<Appointment> getAppointmentsWithStatusFalse() {
        return appointmentRepository.findByStatusFalse();
    }
}