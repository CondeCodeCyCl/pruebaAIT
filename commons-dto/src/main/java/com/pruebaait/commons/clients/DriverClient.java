package com.pruebaait.commons.clients;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.pruebaait.commons.dto.driver.DriverResponse;

@FeignClient(name = "driver-msv")
public interface DriverClient {

	@GetMapping("/api/drivers/{id}")
    DriverResponse getDriverById(@PathVariable UUID id);
	
    @PutMapping("api/drivers/{id}/status")
    void updateDriverStatus(@PathVariable UUID id, @RequestParam Boolean status);
	
}
