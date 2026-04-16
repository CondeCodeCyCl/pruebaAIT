package com.pruebaait.mapper;
import org.springframework.stereotype.Component;
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.entities.Order;

@Component
public class OrderMapper {
	public Order requestToEntity(OrderRequest request) {
        if (request == null) return null;
        
        return Order.builder()
                .origin(request.origin())
                .destination(request.destination())
                .build();
    }

    public OrderResponse entityToResponse(Order entity) {
        if (entity == null) return null;
        
        return new OrderResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getOrigin(),
                entity.getDestination(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
