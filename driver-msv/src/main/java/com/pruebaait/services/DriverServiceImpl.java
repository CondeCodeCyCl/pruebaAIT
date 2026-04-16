package com.pruebaait.services;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pruebaait.dto.DriverRequest;
import com.pruebaait.dto.DriverResponse;
import com.pruebaait.mappers.DriverMapper;
import com.pruebaait.repositories.DriverRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DriverServiceImpl implements DriverService{
	
	private final DriverRepository driverRepository;
	private final DriverMapper driverMapper;

	@Override
	public DriverResponse createDriver(DriverRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DriverResponse> getAllDrivers() {
		log.info("Listado de todos los conductores activos solicitados");

	    return driverRepository.findByActiveTrue() // 1. Traemos solo los conductores activos (Entidades)
	            .stream()                          // 2. Abrimos el flujo de datos
	            .map(driverMapper::entityToResponse) // 3. El Mapper transforma cada Entity a Response
	            .collect(Collectors.toList());     // 4. Volvemos a empaquetar todo en una Lista
	}

	@Override
	public DriverResponse getDriverById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DriverResponse updateDriver(UUID id, DriverRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDriver(UUID id) {
		// TODO Auto-generated method stub
		
	}

	
	

}
