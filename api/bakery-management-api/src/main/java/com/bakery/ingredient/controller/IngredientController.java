package com.bakery.ingredient.controller;

import com.bakery.common.api.ApiResponse;
import com.bakery.common.exception.IdMismatchException;
import com.bakery.common.exception.IdNotAllowedException;
import com.bakery.ingredient.model.dto.IngredientDTO;
import com.bakery.ingredient.model.dto.IngredientListDTO;
import com.bakery.ingredient.model.entity.Ingredient;
import com.bakery.ingredient.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

	private final IngredientService ingredientService;

	@Operation(summary = "Get all ingredient", description = "Retrieve all stored ingredients")
	@GetMapping
	public ResponseEntity<ApiResponse<List<IngredientDTO>>> getAll() {
		return ResponseEntity.ok(ApiResponse.success(ingredientService.getAllIngredients()));
	}

	@Operation(summary = "Get ingredient", description = "Retrieve a stored ingredient")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<IngredientDTO>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(ingredientService.getIngredientById(id)));
	}

	@Operation(summary = "Add ingredient(s)", description = "Register ingredient(s) with its name, purchase unit, and base cost")
	@PostMapping
	public ResponseEntity<ApiResponse<IngredientListDTO>> create(@RequestBody @Valid IngredientListDTO listDto) {
		List<IngredientDTO> dtos = listDto.getIngredients();
		for (IngredientDTO dto : dtos) {
			if (dto.getId() != null) {
				throw new IdNotAllowedException();
			}
		}
		List<IngredientDTO> saved = dtos.stream().map(ingredientService::saveIngredient).toList();
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IngredientListDTO(saved)));
	}

	@Operation(summary = "Update ingredient", description = "Update a stored ingredient")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<IngredientDTO>> update(@PathVariable Long id, @RequestBody @Valid IngredientDTO dto) {
		if (dto.getId() != null && !dto.getId().equals(id)) {
			throw new IdMismatchException(id, dto.getId());
		}
		return ResponseEntity.ok(ApiResponse.success(ingredientService.updateIngredient(id, dto), "Recipe updated"));
	}

	@Operation(summary = "Delete ingredient", description = "Delete a stored ingredient")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
		ingredientService.deleteIngredient(id);
		return ResponseEntity.ok(ApiResponse.success(null, "Recipe deleted"));
	}
}
