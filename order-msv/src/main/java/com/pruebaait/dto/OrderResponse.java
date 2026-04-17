package com.pruebaait.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pruebaait.enums.Status;

public record OrderResponse(
		UUID id,
		Status status,
		String origin,
		String destination,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime createdAt,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime updatedAt
		) {

}
