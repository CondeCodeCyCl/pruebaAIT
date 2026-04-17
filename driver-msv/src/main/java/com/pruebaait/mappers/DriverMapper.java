package com.pruebaait.mappers;
import org.springframework.stereotype.Component;
import com.pruebaait.commons.dto.driver.DriverRequest;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.entities.Driver;

@Component
public class DriverMapper {
	
    public Driver requestToEntity(DriverRequest request) {
        if (request == null) return null;
        
        return Driver.builder()
                .name(request.name())
                .licenseNumber(request.licenseNumber())
                .active(request.active())
                .build();
    }

    public DriverResponse entityToResponse(Driver entity) {
        if (entity == null) return null;
        
        return new DriverResponse(
                entity.getId(),
                entity.getName(),
                entity.getLicenseNumber(),
                entity.getActive()
        );
    }

}
