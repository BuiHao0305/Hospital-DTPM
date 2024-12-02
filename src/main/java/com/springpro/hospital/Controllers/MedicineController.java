package com.springpro.hospital.Controllers;

import com.springpro.hospital.ApiResponse.ApiResponse;
import com.springpro.hospital.Entities.Medicine;
import com.springpro.hospital.Services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // Thêm thuốc
    @PostMapping
    public ResponseEntity<Object> addMedicine(@RequestBody Medicine medicine) {
        try {
            Medicine savedMedicine = medicineService.addMedicine(medicine);
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "Thêm thuốc thành công!", savedMedicine)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(
                    new ApiResponse(false, "Tên thuốc đã tồn tại trong hệ thống.")
            );
        }
    }

    // Cập nhật thông tin thuốc
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMedicine(@PathVariable String id, @RequestBody Medicine medicine) {
        try {
            Medicine updatedMedicine = medicineService.updateMedicine(id, medicine);
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "Cập nhật thuốc thành công!", updatedMedicine)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Dữ liệu không hợp lệ.")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    new ApiResponse(false, "Thuốc trùng với tên thuốc khác trong hệ thống.")
            );
        }
    }

    // Xóa thuốc
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMedicine(@PathVariable String id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "Xóa thuốc thành công.")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    new ApiResponse(false, "Thuốc không tồn tại, không thể xóa.")
            );
        }
    }

    // Lấy danh sách tất cả thuốc
    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        List<Medicine> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    // Lấy thông tin chi tiết thuốc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable String id) {
        return medicineService.getMedicineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
