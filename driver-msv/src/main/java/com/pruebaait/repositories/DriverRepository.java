package com.pruebaait.repositories;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pruebaait.entities.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
	// ¡Aquí aprovechamos la magia de Spring Data JPA!
    // El examen pide: "Listar todos los conductores activos". 
    // Solo con escribir este nombre de método, Spring crea la consulta SQL por debajo:
    List<Driver> findByActiveTrue();
}
