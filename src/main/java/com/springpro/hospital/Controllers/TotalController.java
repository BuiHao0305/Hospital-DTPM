package com.springpro.hospital.Controllers;

import com.springpro.hospital.ApiResponse.TotalCostResponse;
import com.springpro.hospital.Services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TotalController {
    @Autowired
    private PrescriptionService prescriptionService;

    // API GET để lấy tổng chi phí
    @GetMapping("/total-cost")
    public TotalCostResponse getTotalCost() {
        double totalCost = prescriptionService.getTotal(); // Lấy tổng chi phí từ PrescriptionService
        return new TotalCostResponse(totalCost); // Trả về đối tượng TotalCostResponse chứa totalCost
    }
}
