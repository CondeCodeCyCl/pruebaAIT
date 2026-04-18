package com.pruebaait.pruebaUnitaria;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.pruebaait.commons.exceptions.RecursoNoEncontradoException;
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
    void listar_DebeRetornarListaDeConductores() {
        // 1. Arrange (Preparar)
        Driver driver = new Driver();
        driver.setName("Carlos");
        DriverResponse response = new DriverResponse(UUID.randomUUID(), "Carlos", "MX-128565", true);

        when(driverRepository.findAll()).thenReturn(List.of(driver));
        when(driverMapper.entityToResponse(driver)).thenReturn(response);

        // 2. Act (Ejecutar)
        List<DriverResponse> resultado = driverService.getAllDrivers();

        // 3. Assert (Verificar)
        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).name());
        verify(driverRepository).findAll();
    }
    
    @Test
    void createDriver_CaminoFeliz_DebeRetornarDriverActivo() {
        // 1. Arrange (Preparar)
        DriverRequest request = new DriverRequest("Carlos Conde", "MX-128565", true);
        Driver driverMapeado = new Driver();
        DriverResponse responseEsperada = new DriverResponse(UUID.randomUUID(), "Carlos Conde", "MX-128565", true);

        when(driverMapper.requestToEntity(request)).thenReturn(driverMapeado);
        when(driverRepository.save(any(Driver.class))).thenReturn(driverMapeado); // Reutilizamos el objeto para acortar
        when(driverMapper.entityToResponse(driverMapeado)).thenReturn(responseEsperada);

        // 2. Act (Ejecutar)
        DriverResponse responseReal = driverService.createDriver(request);
        
        // 3. Assert (Verificar)
        assertNotNull(responseReal);
        assertTrue(responseReal.active());
        assertEquals("Carlos Conde", responseReal.name());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }
    
    @Test
    void getDriverById_CuandoExiste_DebeRetornarConductor() {
        // 1. Arrange
        UUID id = UUID.randomUUID();
        Driver driver = new Driver();
        driver.setId(id);
        driver.setName("Carlos");

        DriverResponse response = new DriverResponse(id, "Carlos", "MX-123", true);

        // Ojo: Para findById usamos Optional.of()
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        when(driverMapper.entityToResponse(driver)).thenReturn(response);

        // 2. Act
        DriverResponse resultado = driverService.getDriverById(id);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.name());
        verify(driverRepository).findById(id);
    }

    @Test
    void getDriverById_CuandoNoExiste_DebeLanzarExcepcion() {
        // 1. Arrange
        UUID id = UUID.randomUUID();
        // Simulamos que la base de datos no encontró nada (Optional vacío)
        when(driverRepository.findById(id)).thenReturn(Optional.empty());

        // 2 & 3. Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            driverService.getDriverById(id);
        });
        
        // Verificamos que sí intentó buscarlo
        verify(driverRepository).findById(id);
    }

    @Test
    void updateDriverStatus_DebeCambiarEstadoYGuardar() {
        
        UUID id = UUID.randomUUID();
        Driver driver = new Driver();
        driver.setId(id);
        driver.setActive(true);

        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));

        
        driverService.updateDriverStatus(id, false);

        assertFalse(driver.getActive()); 

        verify(driverRepository).save(driver); 
    }
    
}
