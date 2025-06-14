package com.bakery.recipe.model.dto;

import com.bakery.unit.model.dto.UnitDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientComponentDTO {
	private Long ingredientId;
	private String ingredientName;
	private Double quantityUsed;
	private UnitDTO unit;
}
