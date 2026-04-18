package com.pruebaait.services;
import java.util.List;
import java.util.UUID;

import com.pruebaait.commons.dto.driver.DriverRequest;
import com.pruebaait.commons.dto.driver.DriverResponse;

public interface DriverService{

    DriverResponse createDriver(DriverRequest request);
    
    DriverResponse getDriverById(UUID id);
    
    void updateDriverStatus(UUID id, Boolean status);

    List<DriverResponse> getAllDriversActives();
    
    List <DriverResponse> getAllDrivers();
}
