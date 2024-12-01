package com.springpro.hospital.Services;


import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Entities.Prescription;
import com.springpro.hospital.Repositories.MedicineRepository;
import com.springpro.hospital.Repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    // Tạo đơn thuốc mới
    public Prescription createPrescription(Prescription prescription) {
        // Tính tổng chi phí: phí khám + giá thuốc
        double totalCost = prescription.getConsultationFee();
        List<String> medicineIds = prescription.getMedicineIds();

        for (String medicineId : medicineIds) {
            Optional<Medicine> medicine = medicineRepository.findById(medicineId);
            if (medicine.isPresent()) {
                totalCost += medicine.get().getPrice();  // Cộng thêm giá thuốc vào tổng chi phí
            }
        }

        prescription.setTotalCost(totalCost);  // Cập nhật tổng chi phí
        return prescriptionRepository.save(prescription);  // Lưu đơn thuốc vào cơ sở dữ liệu
    }

    // Lấy danh sách đơn thuốc
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Lấy đơn thuốc theo ID
    public Optional<Prescription> getPrescriptionById(String id) {
        return prescriptionRepository.findById(id);
    }

    // Xóa đơn thuốc theo ID
    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }
}