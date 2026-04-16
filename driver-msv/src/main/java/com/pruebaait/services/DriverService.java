package com.pruebaait.services;
import java.util.List;
import java.util.UUID;
import com.pruebaait.dto.DriverRequest;
import com.pruebaait.dto.DriverResponse;

public interface DriverService{

    DriverResponse createDriver(DriverRequest request);

    List<DriverResponse> getAllDrivers(); // O getActiveDrivers() como pedía el examen
    
    DriverResponse getDriverById(UUID id);

    DriverResponse updateDriver(UUID id, DriverRequest request);

    void deleteDriver(UUID id);
}
