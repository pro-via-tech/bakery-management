package com.bakery.ingredient.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientListDTO {
	@Valid
	@NotEmpty(message = "Ingredient list must not be empty")
	private List<IngredientDTO> ingredients;
}

