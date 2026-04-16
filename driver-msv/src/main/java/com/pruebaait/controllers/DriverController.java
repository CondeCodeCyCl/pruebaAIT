package com.pruebaait.controllers;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pruebaait.dto.DriverResponse;
import com.pruebaait.services.DriverService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Validated
public class DriverController {
	
	private final DriverService driverService;
	
	
    @GetMapping()
    public ResponseEntity<List<DriverResponse>> getActiveDrivers() {
        List<DriverResponse> responses = driverService.getAllDrivers(); // O getActiveDrivers() según como lo llamaste
        
        return ResponseEntity.ok(responses);
    }

}
