package com.bakery.recipe.controller;

import com.bakery.common.api.ApiResponse;
import com.bakery.recipe.model.dto.RecipeDTO;
import com.bakery.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeService recipeService;

	@Operation(summary = "Get all recipes", description = "Retrieve all stored recipes")
	@GetMapping
	public ResponseEntity<ApiResponse<List<RecipeDTO>>> getAllRecipes() {
		return ResponseEntity.ok(ApiResponse.success(recipeService.getAllRecipes(), "Fetched all recipes"));
	}

	@Operation(summary = "Get a recipe", description = "Get a recipe")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RecipeDTO>> getRecipe(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(recipeService.getRecipeById(id), "Fetched recipe"));
	}

	@Operation(summary = "Add a new recipe", description = "Add a new recipe")
	@PostMapping
	public ResponseEntity<ApiResponse<RecipeDTO>> createRecipe(@Valid @RequestBody RecipeDTO dto) {
		return ResponseEntity.ok(ApiResponse.success(recipeService.saveRecipe(dto), "Recipe created"));
	}

	@Operation(summary = "Update a recipe", description = "Update a new recipe")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<RecipeDTO>> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDTO dto) {
		return ResponseEntity.ok(ApiResponse.success(recipeService.updateRecipe(id, dto), "Recipe updated"));
	}

	@Operation(summary = "Delete recipe", description = "Delete a stored recipe")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteRecipe(@PathVariable Long id) {
		recipeService.deleteRecipe(id);
		return ResponseEntity.ok(ApiResponse.success(null, "Recipe deleted"));
	}
}
