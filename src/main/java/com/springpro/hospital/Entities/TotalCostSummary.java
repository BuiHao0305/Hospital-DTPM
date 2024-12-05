package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "totalCostSummary")
public class TotalCostSummary {
    @Id
    private String id;
    private double totalCost;


}
