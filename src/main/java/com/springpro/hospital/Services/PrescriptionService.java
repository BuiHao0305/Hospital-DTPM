package com.springpro.hospital.Services;


import com.springpro.hospital.DTO.MedicineDetailDTO;
import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Entities.MedicinePurchase;
import com.springpro.hospital.Entities.Prescription;
import com.springpro.hospital.Repositories.MedicineRepository;
import com.springpro.hospital.Repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    // Tạo đơn thuốc mới
    public Prescription createPrescription(Prescription prescription) {
        double totalCost = prescription.getConsultationFee();

        // Duyệt qua danh sách thuốc mua
        for (MedicinePurchase purchase : prescription.getMedicinePurchases()) {
            Optional<Medicine> medicineOpt = medicineRepository.findById(purchase.getMedicineId());
            if (medicineOpt.isPresent()) {
                Medicine medicine = medicineOpt.get();

                // Tính tiền thuốc theo số lượng
                double medicineCost = medicine.getPrice() * purchase.getQuantity();
                totalCost += medicineCost;

                // Cập nhật tồn kho thuốc (giảm số lượng)
                if (medicine.getQuantity() >= purchase.getQuantity()) {
                    medicine.setQuantity(medicine.getQuantity() - purchase.getQuantity());
                    medicineRepository.save(medicine);
                } else {
                    throw new IllegalArgumentException("Số lượng thuốc không đủ trong kho: " + medicine.getName());
                }

                // Ghi nhận tổng giá cho từng MedicinePurchase
                purchase.setTotalPrice(medicineCost);
            } else {
                throw new IllegalArgumentException("Không tìm thấy thuốc với ID: " + purchase.getMedicineId());
            }
        }

        prescription.setTotalCost(totalCost);
        return prescriptionRepository.save(prescription);
    }

    // Thêm thuốc vào đơn thuốc
    public Prescription addMedicineToPrescription(String prescriptionId, List<MedicinePurchase> medicinePurchases) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(prescriptionId);
        if (!prescriptionOpt.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy đơn thuốc với ID: " + prescriptionId);
        }

        Prescription prescription = prescriptionOpt.get();
        double totalCost = prescription.getTotalCost(); // Lấy tổng chi phí hiện tại

        // Duyệt qua danh sách thuốc mua
        for (MedicinePurchase purchase : medicinePurchases) {
            Optional<Medicine> medicineOpt = medicineRepository.findById(purchase.getMedicineId());
            if (medicineOpt.isPresent()) {
                Medicine medicine = medicineOpt.get();

                // Tính tiền thuốc theo số lượng
                double medicineCost = medicine.getPrice() * purchase.getQuantity();
                totalCost += medicineCost;

                // Cập nhật tồn kho thuốc (giảm số lượng)
                if (medicine.getQuantity() >= purchase.getQuantity()) {
                    medicine.setQuantity(medicine.getQuantity() - purchase.getQuantity());
                    medicineRepository.save(medicine);
                } else {
                    throw new IllegalArgumentException("Số lượng thuốc không đủ trong kho: " + medicine.getName());
                }

                // Ghi nhận tổng giá cho từng MedicinePurchase
                purchase.setTotalPrice(medicineCost);
                prescription.getMedicinePurchases().add(purchase); // Thêm thuốc vào đơn thuốc
            } else {
                throw new IllegalArgumentException("Không tìm thấy thuốc với ID: " + purchase.getMedicineId());
            }
        }

        prescription.setTotalCost(totalCost); // Cập nhật tổng chi phí mới
        return prescriptionRepository.save(prescription); // Lưu đơn thuốc đã cập nhật
    }

    // Lấy danh sách đơn thuốc
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Lấy đơn thuốc theo ID
    public Optional<Prescription> getPrescriptionById(String id) {
        return prescriptionRepository.findById(id);
    }

    // Xóa đơn thuốc
    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }
}