package com.pruebaait.services;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pruebaait.commons.dto.driver.DriverRequest;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.entities.Driver;
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
		log.info("Registrando nuevo conductor: {}", request);
		
		Driver driver = driverMapper.requestToEntity(request);
		
		log.info("Driver registrado existosamente: {}", driver);

		return driverMapper.entityToResponse(driverRepository.save(driver));
	}

	@Override
	@Transactional(readOnly = true)
	public List<DriverResponse> getAllDrivers() {
		log.info("Listado de todos los conductores activos solicitados");

	    return driverRepository.findByActiveTrue() 
	            .stream()                          
	            .map(driverMapper::entityToResponse) 
	            .collect(Collectors.toList());
	}
}
