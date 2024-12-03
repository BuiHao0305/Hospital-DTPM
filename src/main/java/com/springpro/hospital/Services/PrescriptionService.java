package com.springpro.hospital.Services;

import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Entities.MedicinePurchase;
import com.springpro.hospital.Entities.Prescription;
import com.springpro.hospital.Entities.TotalCostSummary;
import com.springpro.hospital.Repositories.MedicineRepository;
import com.springpro.hospital.Repositories.PrescriptionRepository;
import com.springpro.hospital.Repositories.TotalCostSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private TotalCostSummaryRepository totalCostSummaryRepository;

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
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Cập nhật tổng cost nếu status = true
        if (savedPrescription.isStatus()) {
            updateTotalCostSummary();
        }

        return savedPrescription;
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

                // Cập nhật tồn kho thuốc (giảm số lượng) chỉ khi status = true
                if (prescription.isStatus()) {
                    if (medicine.getQuantity() >= purchase.getQuantity()) {
                        medicine.setQuantity(medicine.getQuantity() - purchase.getQuantity());
                        medicineRepository.save(medicine);
                    } else {
                        throw new IllegalArgumentException("Số lượng thuốc không đủ trong kho: " + medicine.getName());
                    }
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
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Cập nhật tổng cost nếu status = true
        if (savedPrescription.isStatus()) {
            updateTotalCostSummary();
        }

        return savedPrescription; // Lưu đơn thuốc đã cập nhật
    }

    // Cập nhật tổng cost cho tất cả Prescription có status = true
    private void updateTotalCostSummary() {
        // Lấy danh sách tất cả các đơn thuốc có status = true
        List<Prescription> prescriptions = prescriptionRepository.findByStatus(true);

        // Tính tổng chi phí từ tất cả các đơn thuốc
        double totalCost = prescriptions.stream()
                .mapToDouble(Prescription::getTotalCost)  // Lấy totalCost từ mỗi đơn thuốc
                .sum();  // Tổng hợp tất cả các totalCost

        // Tìm kiếm hoặc tạo mới đối tượng TotalCostSummary
        Optional<TotalCostSummary> totalCostSummaryOpt = totalCostSummaryRepository.findById("1");

        TotalCostSummary totalCostSummary;
        if (totalCostSummaryOpt.isPresent()) {
            // Nếu bản ghi đã tồn tại, lấy đối tượng
            totalCostSummary = totalCostSummaryOpt.get();
        } else {
            // Nếu không tìm thấy, tạo đối tượng mới với ID cố định là "1"
            totalCostSummary = new TotalCostSummary();
            totalCostSummary.setId("1");  // Gán ID cố định
        }

        // Cập nhật tổng chi phí
        totalCostSummary.setTotalCost(totalCost);

        // Lưu hoặc cập nhật đối tượng TotalCostSummary
        totalCostSummaryRepository.save(totalCostSummary);
    }



    // API để thay đổi status của Prescription
    @Transactional
    public Prescription updateStatus(String prescriptionId, boolean status) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(prescriptionId);
        if (!prescriptionOpt.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy đơn thuốc với ID: " + prescriptionId);
        }

        Prescription prescription = prescriptionOpt.get();
        prescription.setStatus(status);

        // Cập nhật tổng cost khi thay đổi status
        updateTotalCostSummary(); // Cập nhật lại tổng chi phí bất kể status là true hay false

        return prescriptionRepository.save(prescription);
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
