package com.springpro.hospital.Mappers;

import com.springpro.hospital.Constant.MedicineCategory;

public class MedicineCategoryMapper {
    public static String getCategoryName(int category) {
        switch (category) {
            case MedicineCategory.HA_SOT_KHANG_VIEM: return "Thuốc hạ sốt, kháng viêm";
            case MedicineCategory.GIAM_DAU: return "Thuốc giảm đau";
            case MedicineCategory.TIEU_HOA: return "Thuốc tiêu hóa";
            case MedicineCategory.DA_LIEU: return "Thuốc da liễu";
            case MedicineCategory.SAT_TRUNG: return "Thuốc sát trùng";
            case MedicineCategory.VITAMIN: return "Vitamin";
            default: return "Khác";
        }
    }
}
