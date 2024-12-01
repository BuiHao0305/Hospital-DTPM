package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
}