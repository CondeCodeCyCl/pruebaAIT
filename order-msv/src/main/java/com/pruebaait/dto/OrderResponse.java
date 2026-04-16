package com.pruebaait.dto;
import java.time.LocalDateTime;
import java.util.UUID;
import com.pruebaait.enums.Status;

public record OrderResponse(
		UUID id,
		Status status,
		String origin,
		String destination,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
		) {

}
