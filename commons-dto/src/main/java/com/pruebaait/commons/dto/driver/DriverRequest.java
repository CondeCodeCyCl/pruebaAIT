package com.pruebaait.commons.dto.driver;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverRequest(
		
		@NotBlank(message = "El nombre del conductor no puede estar vacío")
	    String name,

	    @NotBlank(message = "El número de licencia es obligatorio")
	    String licenseNumber,

	    @NotNull(message = "El estado del conductor (active) es obligatorio")
	    Boolean active
	    ) {

}
