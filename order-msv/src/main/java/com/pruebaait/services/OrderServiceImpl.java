package com.pruebaait.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.entities.Order;
import com.pruebaait.enums.Status;
import com.pruebaait.exceptions.RecursoNoEncontradoException;
import com.pruebaait.mapper.OrderMapper;
import com.pruebaait.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	@Override
	public OrderResponse createOrder(OrderRequest request) {
		log.info("Registrando nueva orden: {}", request);

		Order order = orderMapper.requestToEntity(request);

		// Toda orden nueva es CREATED
		order.setStatus(Status.CREATED);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		log.info("Order registrado existosamente: {}", order);
		return orderMapper.entityToResponse(orderRepository.save(order));
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		log.info("Listado de todos los orders solicitadas");

		return orderRepository.findAll().stream().map(orderMapper::entityToResponse).collect(Collectors.toList());
	}

	@Override
	public OrderResponse getOrderById(UUID id) {
		log.info("Obtener order por id solicitado");

		Order order = obtenerOrderOException(id);

		return orderMapper.entityToResponse(order);
	}

	private Order obtenerOrderOException(UUID id) {
		log.info("Buscando order con id: {}", id);

		return orderRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada con id: " + id));
	}

	@Override
	public OrderResponse updateOrderStatus(UUID id, Status newStatus) {

		Order order = obtenerOrderOException(id);
		log.info("Actualizando Order con id: {}", id);

		cambioStatus(order.getStatus(), newStatus);

		order.setStatus(newStatus);

		order.setUpdatedAt(LocalDateTime.now());

		log.info("Status actualizado exitosamente");
		return orderMapper.entityToResponse(order);
	}

	private void cambioStatus(Status statusActual, Status statusNuevo) {
		switch (statusActual) {
		case CREATED:
			if (statusNuevo != Status.IN_TRANSIT && statusNuevo != Status.CANCELLED) {
				throw new IllegalArgumentException("Una orden CREATED solo puede pasar a IN_TRANSIT o CANCELLED");
			}
			break;
		case IN_TRANSIT:
			if (statusNuevo != Status.DELIVERED && statusNuevo != Status.CANCELLED) {
				throw new IllegalArgumentException("Una orden IN_TRANSIT solo puede pasar a DELIVERED o CANCELLED");
			}
			break;
		case DELIVERED:
		case CANCELLED:
			throw new IllegalArgumentException(
					"No se puede cambiar el estado de una orden finalizada a :" + statusActual);
		}
	}

	public List<OrderResponse> getOrdersByStatus(Status status) {
		log.info("Buscando órdenes en la base de datos con status: {}", status);

		List<Order> ordersEntity = orderRepository.findByStatus(status);

		return ordersEntity.stream()
				.map(orderMapper::entityToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> findByOrigin(String origin) {
		log.info("Buscando órdenes en la base de datos con origen: {}", origin);

		List<Order> ordersEntity = orderRepository.findByOrigin(origin);

		return ordersEntity.stream()
				.map(orderMapper::entityToResponse)
				.collect(Collectors.toList());
	}

	
	public List<OrderResponse> findByDestination(String destination) {
		log.info("Buscando órdenes en la base de datos con destino: {}", destination);

		List<Order> ordersEntity = orderRepository.findByDestination(destination);

		return ordersEntity.stream()
				.map(orderMapper::entityToResponse)
				.collect(Collectors.toList());
	}

}
