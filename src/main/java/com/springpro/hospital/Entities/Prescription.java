package com.springpro.hospital.Entities;

import com.springpro.hospital.DTO.MedicineDetailDTO;
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
    private List<MedicinePurchase> medicinePurchases; // Danh sách thuốc mua
    private double consultationFee;
    private double totalCost;
    private boolean status =false;

    // Chi tiết thuốc (chỉ để hiển thị, không lưu trong MongoDB)
    private List<MedicineDetailDTO> medicineDetails;
}