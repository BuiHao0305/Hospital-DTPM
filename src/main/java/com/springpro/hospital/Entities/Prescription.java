package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(collection = "prescriptions")
public class Prescription {
    @Id
    private String id;
    private String patientName;
    private List<String> medicineIds;
    private double consultationFee;
    private double totalCost;

    // Getters and Setters
}