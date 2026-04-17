package com.pruebaait.services;
import java.util.List;
import java.util.UUID;
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.enums.Status;

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);

	List<OrderResponse> getAllOrders();

	OrderResponse getOrderById(UUID id);

	OrderResponse updateOrderStatus(UUID id, Status newStatus);
}
