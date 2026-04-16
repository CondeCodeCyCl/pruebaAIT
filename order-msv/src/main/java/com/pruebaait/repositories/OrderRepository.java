package com.pruebaait.repositories;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pruebaait.entities.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{

}
