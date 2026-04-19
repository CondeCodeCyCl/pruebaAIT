package com.pruebaait.pruebaUnitaria;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.pruebaait.commons.dto.driver.DriverRequest;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.entities.Driver;
import com.pruebaait.mappers.DriverMapper;
import com.pruebaait.repositories.DriverRepository;
import com.pruebaait.services.DriverServiceImpl;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

	@Mock
	private DriverRepository driverRepository;

	@Mock
	private DriverMapper driverMapper;

	@InjectMocks
	private DriverServiceImpl driverService;

	@Test
	void ListarConductores() {

		Driver driver = new Driver();
		driver.setName("Carlos");
		driver.setLicenseNumber("MX-128565");
		driver.setActive(true);
		DriverResponse response = new DriverResponse(UUID.randomUUID(), "Carlos", "MX-128565", true);

		when(driverRepository.findAll()).thenReturn(List.of(driver));
		when(driverMapper.entityToResponse(driver)).thenReturn(response);

		List<DriverResponse> resultado = driverService.getAllDrivers();

		assertEquals(1, resultado.size());
		assertEquals("Carlos", resultado.get(0).name());
		verify(driverRepository).findAll();
	}

	@Test
	void crearConductor() {
		//Objeto simulado
		DriverRequest request = new DriverRequest("Carlos Conde", "MX-128565", true);
		Driver driverMapeado = new Driver();

		// Entrenamos los mocks pasando las instancias directamente donde se puede
		when(driverMapper.requestToEntity(request)).thenReturn(driverMapeado);
		when(driverRepository.save(any(Driver.class))).thenReturn(driverMapeado);
		when(driverMapper.entityToResponse(driverMapeado))
				.thenReturn(new DriverResponse(UUID.randomUUID(), "Carlos Conde", "MX-128565", true));

		// Ejecutamos
		DriverResponse responseReal = driverService.createDriver(request);

		// Verificamos
		assertNotNull(responseReal);
		assertTrue(responseReal.active());
		assertEquals("Carlos Conde", responseReal.name());
		verify(driverRepository, times(1)).save(any(Driver.class));
	}

	@Test
	void obtenerConductorPorId() {

		UUID id = UUID.randomUUID();
		Driver driver = new Driver();
		driver.setId(id);
		driver.setName("Carlos");
		driver.setActive(true);

		DriverResponse responseEsperada = new DriverResponse(id, "Carlos", "MX-123", true);

		when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
		when(driverMapper.entityToResponse(driver)).thenReturn(responseEsperada);

		DriverResponse responseReal = driverService.getDriverById(id);

		assertNotNull(responseReal);
		assertEquals("Carlos", responseReal.name());
		verify(driverRepository).findById(id);
	}
}
