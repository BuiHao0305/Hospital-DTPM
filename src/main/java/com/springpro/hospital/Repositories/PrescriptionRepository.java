package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    List<Prescription> findByStatus(boolean status);
}
