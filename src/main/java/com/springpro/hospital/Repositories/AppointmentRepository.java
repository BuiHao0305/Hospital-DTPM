package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByStatusFalse();
    Optional<Appointment> findById(Long id);
}
