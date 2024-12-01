package com.springpro.hospital.Services;

import com.springpro.hospital.Entities.Doctor;
import com.springpro.hospital.Repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(String id, Doctor doctorDetails) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Doctor not found"));

        doctor.setName(doctorDetails.getName());
        doctor.setSpecialty(doctorDetails.getSpecialty());
        doctor.setPhoneNumber(doctorDetails.getPhoneNumber());
        doctor.setRoomNumber(doctorDetails.getRoomNumber());
        doctor.setPosition(doctorDetails.getPosition());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }
}

