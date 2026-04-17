package com.pruebaait.controllers;
import java.util.List;
import java.util.UUID;
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
import com.pruebaait.dto.OrderRequest;
import com.pruebaait.dto.OrderResponse;
import com.pruebaait.enums.Status;
import com.pruebaait.services.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
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
	
	@GetMapping("/filter/origin/{origin}")
	public ResponseEntity<List<OrderResponse>>  getByOrigin(@PathVariable String origin) {
        return ResponseEntity.ok(orderService.findByOrigin(origin));
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
