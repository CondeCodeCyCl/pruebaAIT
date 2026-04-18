package com.pruebaait.commons.dto.driver;

import java.util.UUID;

public record DatosDriver(
		UUID id,
		String name,
	    String licenseNumber
		) {
}