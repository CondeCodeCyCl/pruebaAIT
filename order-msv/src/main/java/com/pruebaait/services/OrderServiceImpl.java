package com.pruebaait.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

		Order order = orderMapper.requestToEntity(request);

		order.setStatus(Status.CREATED);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		log.info("Order registrado existosamente: {}", order);

		Order savedOrder = orderRepository.save(order);

		
		return orderMapper.entityToResponse(savedOrder, null);
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		log.info("Listado de todos los orders solicitadas");

		return orderRepository.findAll().stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	@Override
	public OrderResponse getOrderById(UUID id) {
		log.info("Obtener order por id solicitado");

		Order order = obtenerOrderOException(id);

		DriverResponse driver = driverClient.getDriverById(order.getIdDriver());

		return orderMapper.entityToResponse(order, driver);
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

		if (order.getIdDriver() != null) {
			if (newStatus == Status.IN_TRANSIT) {

				driverClient.updateDriverStatus(order.getIdDriver(), false);
			} else if (newStatus == Status.DELIVERED || newStatus == Status.CANCELLED) {

				driverClient.updateDriverStatus(order.getIdDriver(), true);
			}
		}

		log.info("Status actualizado exitosamente");
		DriverResponse driver = driverClient.getDriverById(order.getIdDriver());

		return orderMapper.entityToResponse(order, driver);
	}

	private void cambioStatus(Status statusActual, Status statusNuevo) {
		switch (statusActual) {
		case CREATED:
			if (statusNuevo != Status.IN_TRANSIT && statusNuevo != Status.CANCELLED) {
				throw new IllegalStateException("Una orden CREATED solo puede pasar a IN_TRANSIT o CANCELLED");
			}
			break;
		case IN_TRANSIT:
			if (statusNuevo != Status.DELIVERED && statusNuevo != Status.CANCELLED) {
				throw new IllegalStateException("Una orden IN_TRANSIT solo puede pasar a DELIVERED o CANCELLED");
			}
			break;
		case DELIVERED:
		case CANCELLED:
			throw new IllegalStateException("No se puede cambiar el estado de una orden finalizada a :" + statusActual);
		}
	}

	public List<OrderResponse> getOrdersByStatus(Status status) {
		log.info("Buscando órdenes en la base de datos con status: {}", status);

		List<Order> ordersEntity = orderRepository.findByStatus(status);

		return ordersEntity.stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByOrigin(String origin) {
		log.info("Buscando órdenes en la base de datos con origen: {}", origin);

		List<Order> ordersEntity = orderRepository.findByOriginContainingIgnoreCase(origin);

		return ordersEntity.stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByDestination(String destination) {
		log.info("Buscando órdenes en la base de datos con destino: {}", destination);

		List<Order> ordersEntity = orderRepository.findByDestinationContainingIgnoreCase(destination);

		return ordersEntity.stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByStatus(Status status) {
		log.info("Buscando órdenes en la base de datos con estatus: {}", status);

		List<Order> ordersEntity = orderRepository.findByStatus(status);

		return ordersEntity.stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getOrderByFecha(LocalDate fecha) {
		log.info("Buscando órdenes para el día: {}", fecha);

		// Creamos el rango: 2026-04-16 00:00:00.000 hasta 2026-04-16 23:59:59.999
		LocalDateTime inicio = fecha.atStartOfDay();
		LocalDateTime fin = fecha.atTime(23, 59, 59, 999999999);

		return orderRepository.findByCreatedAtBetween(inicio, fin).stream()
				.map(order -> orderMapper.entityToResponse(order, obtenerDriverResponse(order.getIdDriver())))
				.collect(Collectors.toList());
	}

	private DriverResponse obtenerDriverResponse(UUID idDriver) {
		if (idDriver == null)
			return null;
		return driverClient.getDriverById(idDriver);
	}

	@Override
	public OrderResponse asignarConductor(UUID orderId, UUID driverId, MultipartFile pdf, MultipartFile image) {
		Order order = obtenerOrderOException(orderId);

		if (order.getStatus() != Status.CREATED) {
			throw new IllegalStateException("Solo se pueden asignar conductores a órdenes en estado CREATED");
		}

		DriverResponse driver = obtenerDriverResponse(driverId);

		if (driver == null || Boolean.FALSE.equals(driver.active())) {
			throw new IllegalStateException("El conductor no existe o no está activo");
		}

		// Validación de formatos PDF y IMAGEN
	    if (pdf == null || pdf.isEmpty() || !pdf.getContentType().equals("application/pdf")) {
	        throw new IllegalArgumentException("El archivo debe ser un PDF válido");
	    }

	    if (image == null || image.isEmpty() || !image.getContentType().startsWith("image/")) {
	        String contentType = image.getContentType();
	        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
	             throw new IllegalArgumentException("La imagen debe ser formato JPG o PNG");
	        }
	    }

	    try {
	    	
	        //Guardamos en la base de datos
	        order.setFilePdf(pdf.getBytes());
	        order.setFileImage(image.getBytes());
	        log.info("Archivos convertidos a bytes y listos para persistir");
	    } catch (IOException e) {
	        log.error("Error al procesar los archivos: {}", e.getMessage());
	        throw new RuntimeException("Error técnico al leer el contenido de los archivos");
	    }

		driverClient.updateDriverStatus(driverId, false);

		order.setIdDriver(driverId);
		order.setUpdatedAt(LocalDateTime.now());
		Order updatedOrder = orderRepository.save(order);

		return orderMapper.entityToResponse(updatedOrder, driver);
	}
}