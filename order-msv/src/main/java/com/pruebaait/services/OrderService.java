package com.pruebaait.services;
import java.util.List;
import java.util.UUID;
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.enums.Status;

public interface OrderService {

	OrderResponse createDriver(OrderRequest request);

	List<OrderResponse> getAllOrders();

	OrderResponse getById(UUID id);

	OrderResponse update(OrderRequest request, UUID id);

	OrderResponse updateOrderStatus(UUID id, Status newStatus);
}
