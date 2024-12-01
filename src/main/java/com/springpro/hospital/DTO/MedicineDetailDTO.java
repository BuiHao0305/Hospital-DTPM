package com.springpro.hospital.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineDetailDTO {
    private String id;
    private String name;
    private double price;
    private int quantity;
}