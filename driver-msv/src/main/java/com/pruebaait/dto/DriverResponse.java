package com.pruebaait.dto;
import java.util.UUID;

public record DriverResponse(
		UUID id,
		String name,
	    String licenseNumber,
	    Boolean active
		) {

}
