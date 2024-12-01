package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicineRepository extends MongoRepository<Medicine, String> {
}
