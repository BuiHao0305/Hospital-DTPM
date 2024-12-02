package com.springpro.hospital.Services;

import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Mappers.MedicineCategoryMapper;
import com.springpro.hospital.Repositories.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    // Thêm thuốc mới
    public Medicine addMedicine(Medicine medicine) {
        // Kiểm tra tên thuốc đã tồn tại chưa
        if (medicineRepository.existsByName(medicine.getName())) {
            throw new RuntimeException("Thuốc với tên này đã tồn tại.");
        }

        validateCategory(medicine.getCategory());
        return medicineRepository.save(medicine);
    }

    // Cập nhật thông tin thuốc
    public Medicine updateMedicine(String id, Medicine medicineDetails) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Thuốc không tồn tại"));

        // Nếu tên thuốc thay đổi, kiểm tra tên thuốc mới có bị trùng không
        if (!medicine.getName().equals(medicineDetails.getName()) &&
                medicineRepository.existsByName(medicineDetails.getName())) {
            throw new RuntimeException("Thuốc với tên này đã tồn tại.");
        }

        medicine.setName(medicineDetails.getName());
        medicine.setPrice(medicineDetails.getPrice());
        medicine.setDescription(medicineDetails.getDescription());
        medicine.setQuantity(medicineDetails.getQuantity());
        validateCategory(medicineDetails.getCategory());
        medicine.setCategory(medicineDetails.getCategory());

        return medicineRepository.save(medicine);
    }

    public void deleteMedicine(String id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll().stream().map(this::mapCategoryName).collect(Collectors.toList());
    }

    public Optional<Medicine> getMedicineById(String id) {
        return medicineRepository.findById(id).map(this::mapCategoryName);
    }

    // Ánh xạ tên thể loại cho thuốc
    private Medicine mapCategoryName(Medicine medicine) {

        medicine.setDescription(medicine.getDescription());
        return medicine;
    }

    // Xác thực category hợp lệ
    private void validateCategory(int category) {
        if (category < 0 || category > 6) { // Giả sử 6 là mã thể loại cuối cùng (Vitamin)
            throw new IllegalArgumentException("Loại thuốc không hợp lệ");
        }
    }
}
