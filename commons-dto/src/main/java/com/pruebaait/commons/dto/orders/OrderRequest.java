package com.pruebaait.commons.dto.orders;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
		@NotBlank(message = "El origen de la orden no puede estar vacío")
	    String origin,
	    
		@NotBlank(message = "El destino de la orden no puede estar vacío")
	    String destination,
		
	    @NotNull(message = "El id es requerido")
		UUID idDriver
		) {

}
