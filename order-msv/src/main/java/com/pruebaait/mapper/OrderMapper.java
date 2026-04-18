package com.pruebaait.mapper;
import org.springframework.stereotype.Component;
import com.pruebaait.commons.dto.driver.DatosDriver;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.commons.dto.orders.OrderRequest;
import com.pruebaait.commons.dto.orders.OrderResponse;
import com.pruebaait.entities.Order;

@Component
public class OrderMapper {
	public Order requestToEntity(OrderRequest request) {
        if (request == null) return null;
        
        return Order.builder()
                .origin(request.origin())
                .destination(request.destination())
    			    .idDriver(request.idDriver())

                .build();
    }

    public OrderResponse entityToResponse(Order entity, DriverResponse driver) {
        if (entity == null) return null;
        
        return new OrderResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getOrigin(),
                entity.getDestination(),
                driverResponseToDatosDriver(driver),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    private DatosDriver driverResponseToDatosDriver(DriverResponse driver) {
		if (driver == null) return null;
		return new DatosDriver(
				driver.id(),
				driver.name(),
				driver.licenseNumber());
	}
}
