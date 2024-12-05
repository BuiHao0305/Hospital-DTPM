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
                purchase.setMedicineName(medicine.getName());
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

        // Lưu đơn thuốc
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Cập nhật tổng cost nếu status = true
        if (savedPrescription.isStatus()) {
            updateTotalCostSummary();
        }

        return savedPrescription;
    }

    // Thêm thuốc vào đơn thuốc
    public Prescription addMedicineToPrescription(String prescriptionId, Prescription prescriptionRequest) {
        // Tìm kiếm đơn thuốc theo ID
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(prescriptionId);
        if (!prescriptionOpt.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy đơn thuốc với ID: " + prescriptionId);
        }

        Prescription prescription = prescriptionOpt.get();

        // Cập nhật thông tin từ request vào đơn thuốc hiện tại
        prescription.setPatientName(prescriptionRequest.getPatientName());
        prescription.setConsultationFee(prescriptionRequest.getConsultationFee());
        prescription.setPhoneNumber(prescriptionRequest.getPhoneNumber());
        prescription.setAppointmentDate(prescriptionRequest.getAppointmentDate());

        double totalCost = prescription.getTotalCost(); // Lấy tổng chi phí hiện tại

        // Duyệt qua danh sách thuốc mua và thực hiện các thao tác cần thiết
        for (MedicinePurchase purchase : prescriptionRequest.getMedicinePurchases()) {
            Optional<Medicine> medicineOpt = medicineRepository.findById(purchase.getMedicineId());
            if (medicineOpt.isPresent()) {
                Medicine medicine = medicineOpt.get();
                purchase.setMedicineName(medicine.getName());  // Gán tên thuốc từ đối tượng Medicine

                // Tính tiền thuốc theo số lượng và thêm vào tổng chi phí đơn thuốc
                double medicineCost = medicine.getPrice() * purchase.getQuantity();
                totalCost += medicineCost;

                // Kiểm tra số lượng thuốc trong kho và cập nhật nếu đủ
                if (medicine.getQuantity() >= purchase.getQuantity()) {
                    // Giảm số lượng tồn kho nếu đủ thuốc
                    medicine.setQuantity(medicine.getQuantity() - purchase.getQuantity());
                    medicineRepository.save(medicine);  // Lưu thay đổi vào kho thuốc
                } else {
                    throw new IllegalArgumentException("Số lượng thuốc không đủ trong kho: " + medicine.getName());
                }

                // Ghi nhận tổng tiền cho từng MedicinePurchase và thêm vào danh sách
                purchase.setTotalPrice(medicineCost);
                prescription.getMedicinePurchases().add(purchase);  // Thêm thuốc vào đơn thuốc
            } else {
                throw new IllegalArgumentException("Không tìm thấy thuốc với ID: " + purchase.getMedicineId());
            }
        }

        // Cập nhật lại tổng chi phí của đơn thuốc
        prescription.setTotalCost(totalCost);

        // Lưu lại đơn thuốc đã cập nhật vào cơ sở dữ liệu
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Cập nhật tổng chi phí nếu đơn thuốc đã được xác nhận
        if (savedPrescription.isStatus()) {
            updateTotalCostSummary();  // Nếu trạng thái đơn thuốc là true, cập nhật tổng chi phí tổng
        }

        return savedPrescription;  // Trả về đơn thuốc đã được lưu
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
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        for (Prescription prescription : prescriptions) {
            for (MedicinePurchase purchase : prescription.getMedicinePurchases()) {
                Optional<Medicine> medicineOpt = medicineRepository.findById(purchase.getMedicineId());
                if (medicineOpt.isPresent()) {
                    Medicine medicine = medicineOpt.get();
                    purchase.setMedicineName(medicine.getName()); // Cập nhật tên thuốc vào purchase
                }
            }
        }
        return prescriptions;
    }

    // Lấy đơn thuốc theo ID
    public Optional<Prescription> getPrescriptionById(String id) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            for (MedicinePurchase purchase : prescription.getMedicinePurchases()) {
                Optional<Medicine> medicineOpt = medicineRepository.findById(purchase.getMedicineId());
                if (medicineOpt.isPresent()) {
                    Medicine medicine = medicineOpt.get();
                    purchase.setMedicineName(medicine.getName()); // Gán tên thuốc cho purchase
                }
            }
        }
        return prescriptionOpt;
    }

    // Xóa đơn thuốc
    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }
    // Phương thức lấy tổng chi phí tổng
    public double getTotal() {
        // Lấy tổng chi phí từ đối tượng TotalCostSummary
        Optional<TotalCostSummary> totalCostSummaryOpt = totalCostSummaryRepository.findById("1");
        if (totalCostSummaryOpt.isPresent()) {
            return totalCostSummaryOpt.get().getTotalCost();
        }
        return 0.0; // Trả về 0 nếu không tìm thấy bản ghi
    }
}
