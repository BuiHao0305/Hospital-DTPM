package com.springpro.hospital.Repositories;

import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Entities.Prescription;
import com.springpro.hospital.Entities.TotalCostSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TotalCostSummaryRepository extends MongoRepository<TotalCostSummary, String> {

}
