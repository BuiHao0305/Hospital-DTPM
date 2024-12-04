package com.springpro.hospital.Entities;

import com.springpro.hospital.DTO.MedicineDetailDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Document(collection = "prescriptions")
public class Prescription {
    @Id
    private String id;
    private String patientName;
    private String phoneNumber; // Số điện thoại
    private LocalDate appointmentDate; // Ngày khám
    private List<MedicinePurchase> medicinePurchases; // Danh sách thuốc mua
    private double consultationFee;
    private double totalCost;
    private boolean status =false;

    // Chi tiết thuốc (chỉ để hiển thị, không lưu trong MongoDB)
    private List<MedicineDetailDTO> medicineDetails;
    private String appointmentId;
}