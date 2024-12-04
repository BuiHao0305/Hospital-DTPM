package com.springpro.hospital.Controllers;

import com.springpro.hospital.Entities.Appointment;
import com.springpro.hospital.Services.AppointmentService;
import com.springpro.hospital.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    // Thêm lịch khám
    @PostMapping
    public ResponseEntity<ApiResponse> addAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment newAppointment = appointmentService.addAppointment(appointment);
            ApiResponse response = new ApiResponse(true, "Thêm lịch khám thành công", newAppointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Lỗi khi thêm lịch khám: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Sửa lịch khám
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAppointment(@PathVariable String id, @RequestBody Appointment appointment) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
            ApiResponse response = new ApiResponse(true, "Lịch khám cập nhật thành công", updatedAppointment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, "Error: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error updating appointment: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Xóa lịch khám
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable String id) {
        try {
            appointmentService.deleteAppointment(id);
            ApiResponse response = new ApiResponse(true, "Appointment deleted successfully", null);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, "Error: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error deleting appointment: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Lấy danh sách lịch khám
    @GetMapping
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            ApiResponse response = new ApiResponse(true, "Appointments retrieved successfully", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error retrieving appointments: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Lấy chi tiết lịch khám
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable String id) {
        try {
            Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
            if (appointment.isPresent()) {
                ApiResponse response = new ApiResponse(true, "Appointment found", appointment.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = new ApiResponse(false, "Appointment not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error retrieving appointment: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Cập nhật trạng thái lịch khám thành true
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatusToTrue(@PathVariable String id) {
        try {
            Appointment updatedAppointment = appointmentService.updateStatusToTrue(id);
            ApiResponse response = new ApiResponse(true, "Lịch khám ddowjc", updatedAppointment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, "Error: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error updating status: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getstatus")
    public ResponseEntity<ApiResponse> getPendingAppointments() {
        try {
            List<Appointment> pendingAppointments = appointmentService.getAppointmentsWithStatusFalse();
            ApiResponse response = new ApiResponse(true, "Pending appointments retrieved", pendingAppointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error retrieving pending appointments: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
