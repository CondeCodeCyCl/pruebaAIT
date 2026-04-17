package com.pruebaait.services;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pruebaait.commons.clients.DriverClient;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.commons.dto.orders.OrderRequest;
import com.pruebaait.commons.dto.orders.OrderResponse;
import com.pruebaait.entities.Order;
import com.pruebaait.commons.enums.Status;
import com.pruebaait.commons.exceptions.*;
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
	private final DriverClient driverClient;

	@Override
	public OrderResponse createOrder(OrderRequest request) {
		log.info("Registrando nueva orden: {}", request);
		
	    DriverResponse driver = driverClient.getDriverById(request.idDriver());

	   // Validamos que el conductor este activo.
	    if (driver == null || Boolean.FALSE.equals(driver.active())) {
	        throw new IllegalArgumentException("El conductor no está activo o no existe");
	    }

		Order order = orderMapper.requestToEntity(request);
		
		order.setStatus(Status.CREATED); // Estado al crear por default es CREATED
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

		return ordersEntity.stream().map(orderMapper::entityToResponse).collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByOrigin(String origin) {
		log.info("Buscando órdenes en la base de datos con origen: {}", origin);

		List<Order> ordersEntity = orderRepository.findByOriginContainingIgnoreCase(origin);

		return ordersEntity.stream().map(orderMapper::entityToResponse).collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByDestination(String destination) {
		log.info("Buscando órdenes en la base de datos con destino: {}", destination);

		List<Order> ordersEntity = orderRepository.findByDestinationContainingIgnoreCase(destination);

		return ordersEntity.stream().map(orderMapper::entityToResponse).collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByStatus(Status status) {
		log.info("Buscando órdenes en la base de datos con estatus: {}", status);

		List<Order> ordersEntity = orderRepository.findByStatus(status);

		return ordersEntity.stream().map(orderMapper::entityToResponse).collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByFecha(LocalDate fecha) {
		log.info("Buscando órdenes para el día: {}", fecha);

	    // Creamos el rango: 2026-04-16 00:00:00.000 hasta 2026-04-16 23:59:59.999
	    LocalDateTime inicio = fecha.atStartOfDay(); 
	    LocalDateTime fin = fecha.atTime(23, 59, 59, 999999999);

	    return orderRepository.findByCreatedAtBetween(inicio, fin)
	            .stream()
	            .map(orderMapper::entityToResponse)
	            .collect(Collectors.toList());
	}
}