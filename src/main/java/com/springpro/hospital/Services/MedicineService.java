package com.springpro.hospital.Services;

import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Repositories.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    // Thêm thuốc mới
    public Medicine addMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // Cập nhật thông tin thuốc
    public Medicine updateMedicine(String id, Medicine medicineDetails) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Medicine not found"));

        medicine.setName(medicineDetails.getName());
        medicine.setPrice(medicineDetails.getPrice());
        medicine.setDescription(medicineDetails.getDescription());
        medicine.setQuantity(medicineDetails.getQuantity());

        return medicineRepository.save(medicine);
    }

    public void deleteMedicine(String id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Optional<Medicine> getMedicineById(String id) {
        return medicineRepository.findById(id);
    }
}
