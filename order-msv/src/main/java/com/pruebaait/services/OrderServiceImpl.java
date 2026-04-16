package com.pruebaait.services;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.entities.Order;
import com.pruebaait.enums.Status;
import com.pruebaait.mapper.OrderMapper;
import com.pruebaait.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService{
	
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	@Override
	public OrderResponse createDriver(OrderRequest request) {
		log.info("Registrando nueva orden: {}", request);
		
		Order order = orderMapper.requestToEntity(request);
		
		log.info("Order registrado existosamente: {}", order);
		return orderMapper.entityToResponse(orderRepository.save(order));
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse getById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse update(OrderRequest request, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse updateOrderStatus(UUID id, Status newStatus) {
		// TODO Auto-generated method stub
		return null;
	}

}
