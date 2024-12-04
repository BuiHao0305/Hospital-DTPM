package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    private String patientName;
    private String phoneNumber;
    private LocalDate appointmentDate;
    private String notes;
    private Boolean status = false;

// Getters and Setters
}
