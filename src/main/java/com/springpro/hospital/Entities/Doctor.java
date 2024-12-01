package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String name;
    private String specialty;
    private String phoneNumber;
    private String roomNumber;
    private String position;


}
