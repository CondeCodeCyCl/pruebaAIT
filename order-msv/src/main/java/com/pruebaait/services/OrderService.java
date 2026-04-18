package com.pruebaait.services;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.pruebaait.commons.dto.orders.OrderRequest;  
import com.pruebaait.commons.dto.orders.OrderResponse;
import com.pruebaait.commons.enums.Status;  

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);

	List<OrderResponse> getAllOrders();

	OrderResponse getOrderById(UUID id);

	OrderResponse updateOrderStatus(UUID id, Status newStatus);
	
	List<OrderResponse> getOrderByOrigin(String origin);
	
	List<OrderResponse> getOrderByDestination(String destination);
	
	List<OrderResponse> getOrderByStatus(Status status);
	
	List<OrderResponse> getOrderByFecha(LocalDate fecha);
	
	OrderResponse asignarConductor(UUID orderId, UUID driverId, MultipartFile pdf, MultipartFile image);
}
