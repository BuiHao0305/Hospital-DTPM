package com.springpro.hospital.Controllers;

import com.springpro.hospital.Entities.Prescription;
import com.springpro.hospital.Entities.MedicinePurchase;
import com.springpro.hospital.Services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // Tạo đơn thuốc mới
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        try {
            Prescription createdPrescription = prescriptionService.createPrescription(prescription);
            return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Thêm thuốc vào đơn thuốc
    @PostMapping("/{id}/addMedicines")
    public ResponseEntity<Prescription> addMedicinesToPrescription(@PathVariable String id,
                                                                   @RequestBody List<MedicinePurchase> medicinePurchases) {
        try {
            Prescription updatedPrescription = prescriptionService.addMedicineToPrescription(id, medicinePurchases);
            return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Cập nhật trạng thái đơn thuốc
    @PatchMapping("/{id}/status")
    public ResponseEntity<Prescription> updatePrescriptionStatus(@PathVariable String id, @RequestParam boolean status) {
        try {
            Prescription updatedPrescription = prescriptionService.updateStatus(id, status);
            return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách đơn thuốc
    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    // Lấy chi tiết đơn thuốc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable String id) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        return prescription.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Xóa đơn thuốc
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable String id) {
        try {
            prescriptionService.deletePrescription(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
