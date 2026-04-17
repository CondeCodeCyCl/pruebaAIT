package com.pruebaait.repositories;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.pruebaait.entities.Order;
import com.pruebaait.enums.Status;



public interface OrderRepository extends JpaRepository<Order, UUID>{

	List<Order> findByStatus(Status status);

	List<Order> findByOrigin(String origin);

	List<Order> findByDestination(String destination);
		
	@Query("SELECT o FROM Order o WHERE CAST(o.createdAt AS date) = :fecha")
	List<Order> findByFecha(LocalDate fecha);
}
