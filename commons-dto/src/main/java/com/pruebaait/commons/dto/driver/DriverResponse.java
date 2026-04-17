package com.pruebaait.commons.dto.driver;
import java.util.UUID;

public record DriverResponse(
		UUID id,
		String name,
	    String licenseNumber,
	    Boolean active
		) {

}
