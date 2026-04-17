package com.pruebaait.commons.dto.orders;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
		@NotBlank(message = "El origen de la orden no puede estar vacío")
	    String origin,
	    
		@NotBlank(message = "El destino de la orden no puede estar vacío")
	    String destination
		) {

}
