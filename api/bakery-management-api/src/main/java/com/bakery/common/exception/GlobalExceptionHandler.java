package com.bakery.common.exception;

import com.bakery.common.api.ApiResponse;
import com.bakery.common.api.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
		List<ErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
			.map(err -> ErrorDto.of(
				HttpStatus.BAD_REQUEST.value(),
				"VALIDATION_ERROR",
				err.getField(),
				err.getDefaultMessage()
			))
			.toList();

		return ResponseEntity.badRequest().body(ApiResponse.error("Validation failed", errors));
	}

	@ExceptionHandler(IdNotAllowedException.class)
	public ResponseEntity<ApiResponse<Object>> handleIdNotAllowed(IdNotAllowedException ex) {
		ErrorDto error = ErrorDto.general(HttpStatus.BAD_REQUEST.value(), "ID NOT ALLOWED", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage(), List.of(error)));
	}

	@ExceptionHandler(IngredientNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleIngredientNotFound(IngredientNotFoundException ex) {
		ErrorDto error = ErrorDto.general(HttpStatus.NOT_FOUND.value(), "INGREDIENT_NOT_FOUND", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(), List.of(error)));
	}

	@ExceptionHandler(RecipeNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleRecipeNotFound(RecipeNotFoundException ex) {
		ErrorDto error = ErrorDto.general(HttpStatus.NOT_FOUND.value(), "RECIPE_NOT_FOUND", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(), List.of(error)));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ErrorDto error = ErrorDto.general(HttpStatus.CONFLICT.value(), "DATA_INTEGRITY_VIOLATION", "Data conflict or constraint violation");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Conflict occurred", List.of(error)));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
		ErrorDto error = ErrorDto.general(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("An unexpected error occurred", List.of(error)));
	}
}

