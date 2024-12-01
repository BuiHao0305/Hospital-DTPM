package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "medicines")
public class Medicine {
    @Id
    private String id;
    private String name;
    private double price;
    private String description;
    private int quantity;

    // Getters and Setters

}