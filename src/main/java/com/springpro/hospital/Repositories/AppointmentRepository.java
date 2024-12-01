package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
}
