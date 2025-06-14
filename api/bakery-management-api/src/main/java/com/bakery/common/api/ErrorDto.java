package com.bakery.common.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ErrorDto {
	private final int status;
	private final String code;
	private final String field; // Optional
	private final String message;

	public static ErrorDto of(int status, String code, String field, String message) {
		return ErrorDto.builder()
			.status(status)
			.code(code)
			.field(field)
			.message(message)
			.build();
	}

	public static ErrorDto general(int status, String code, String message) {
		return ErrorDto.builder()
			.status(status)
			.code(code)
			.message(message)
			.build();
	}
}

