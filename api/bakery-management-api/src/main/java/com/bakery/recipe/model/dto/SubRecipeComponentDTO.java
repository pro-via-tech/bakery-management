package com.bakery.recipe.model.dto;

import com.bakery.unit.model.dto.UnitDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubRecipeComponentDTO {
	private Long subRecipeId;
	private String subRecipeName;
	private Double quantityUsed;
	private UnitDTO unit;
	private List<IngredientComponentDTO> ingredients; // New field
}
