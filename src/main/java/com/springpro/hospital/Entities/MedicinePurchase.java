package com.springpro.hospital.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicinePurchase {
    private String medicineId;  // ID của thuốc
    private int quantity;       // Số lượng mua
    private double totalPrice;  // Tổng giá tiền thuốc
}