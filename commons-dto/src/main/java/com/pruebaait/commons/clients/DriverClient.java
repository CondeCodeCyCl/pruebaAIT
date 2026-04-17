package com.pruebaait.commons.clients;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.pruebaait.commons.dto.driver.DriverResponse;


@FeignClient(name = "driver-msv",url = "http://localhost:8081")
public interface DriverClient {

	@GetMapping("{id}")
    DriverResponse getDriverById(@PathVariable UUID id);
}
