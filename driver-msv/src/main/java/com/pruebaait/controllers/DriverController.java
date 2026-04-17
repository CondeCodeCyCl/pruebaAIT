package com.pruebaait.controllers;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.commons.dto.driver.DriverRequest;
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
        List<DriverResponse> responses = driverService.getAllDrivers();
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable UUID id) {
        return ResponseEntity.ok(driverService.getDriverById(id)); 
    }
    
    @PostMapping()
    public ResponseEntity<DriverResponse> create (
    		@Validated @RequestBody DriverRequest request) {
    	return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(request));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDriverStatus(@PathVariable UUID id, @RequestParam Boolean status) {
        driverService.updateDriverStatus(id, status);
        return ResponseEntity.noContent().build();
    }

}
