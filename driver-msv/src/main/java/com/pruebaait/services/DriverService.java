package com.pruebaait.services;
import java.util.List;
import com.pruebaait.commons.dto.driver.DriverRequest;
import com.pruebaait.commons.dto.driver.DriverResponse;

public interface DriverService{

    DriverResponse createDriver(DriverRequest request);

    List<DriverResponse> getAllDrivers();
    
}
