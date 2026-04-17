package com.pruebaait.repositories;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pruebaait.entities.Order;
import com.pruebaait.commons.enums.Status;



public interface OrderRepository extends JpaRepository<Order, UUID>{

	List<Order> findByStatus(Status status);

	List<Order> findByOriginContainingIgnoreCase(String origin);

	List<Order> findByDestinationContainingIgnoreCase(String destination);
		
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
