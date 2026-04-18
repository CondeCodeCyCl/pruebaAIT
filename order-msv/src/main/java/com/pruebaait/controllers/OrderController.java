package com.pruebaait.controllers;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pruebaait.commons.dto.orders.OrderRequest;  
import com.pruebaait.commons.dto.orders.OrderResponse;
import com.pruebaait.commons.enums.Status;
import com.pruebaait.services.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Validated
public class OrderController{
	private final OrderService orderService;

	@GetMapping()
	public ResponseEntity<List<OrderResponse>> getAllOrders() {
		List<OrderResponse> responses = orderService.getAllOrders();

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}
	
	@GetMapping("/filter/origin")
	public ResponseEntity<List<OrderResponse>>  getOrderByOrigin(@RequestParam String origin) {
        return ResponseEntity.ok(orderService.getOrderByOrigin(origin));
	}
	
	@GetMapping("/filter/destination")
	public ResponseEntity<List<OrderResponse>>  getOrderByDestination(@RequestParam String destination) {
        return ResponseEntity.ok(orderService.getOrderByDestination(destination));
	}
	
	@GetMapping("/filter/status")
	public ResponseEntity<List<OrderResponse>>  getOrderByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(orderService.getOrderByStatus(status));
	}
	
	
	@GetMapping("/filter/fecha")
	public ResponseEntity<List<OrderResponse>> getOrderByFecha(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		return ResponseEntity.ok(orderService.getOrderByFecha(fecha));
	}
	

	@PostMapping()
	public ResponseEntity<OrderResponse> create(@Validated @RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateStatus(@PathVariable UUID id, @RequestParam Status status) {

		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

}
