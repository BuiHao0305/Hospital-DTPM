package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
}
