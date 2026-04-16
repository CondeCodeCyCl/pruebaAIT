package com.pruebaait.services;
import java.util.List;
import com.pruebaait.dto.DriverRequest;
import com.pruebaait.dto.DriverResponse;

public interface DriverService{

    DriverResponse createDriver(DriverRequest request);

    List<DriverResponse> getAllDrivers();
    
}
