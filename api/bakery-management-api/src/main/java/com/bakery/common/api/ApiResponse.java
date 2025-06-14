package com.bakery.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
	private final boolean success;
	private final String message;
	private final T data;
	private final List<ErrorDto> errors;
	private final Instant timestamp;

	public static <T> ApiResponse<T> success(T data, String message) {
		return ApiResponse.<T>builder()
			.success(true)
			.message(message)
			.data(data)
			.timestamp(Instant.now())
			.build();
	}

	public static <T> ApiResponse<T> success(T data) {
		return success(data, null);
	}

	public static <T> ApiResponse<T> error(String message, List<ErrorDto> errors) {
		return ApiResponse.<T>builder()
			.success(false)
			.message(message)
			.errors(errors)
			.timestamp(Instant.now())
			.build();
	}

	public static <T> ApiResponse<T> error(String message) {
		return error(message, null);
	}
}