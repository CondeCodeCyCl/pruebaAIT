package com.pruebaait.repositories;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pruebaait.entities.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
	
    List<Driver> findByActiveTrue();
}
