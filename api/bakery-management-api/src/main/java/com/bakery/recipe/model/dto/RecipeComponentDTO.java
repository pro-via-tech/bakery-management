package com.bakery.recipe.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeComponentDTO {
	private Long ingredientId;
	private Long subRecipeId;
	private Double quantityUsed;
}

