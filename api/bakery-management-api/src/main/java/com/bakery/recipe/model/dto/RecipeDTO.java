package com.bakery.recipe.model.dto;

import com.bakery.unit.model.dto.UnitDTO;
import com.bakery.unit.model.entity.Unit;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeDTO {
	private Long id;
	private String name;
	private Double yieldAmount;
	private UnitDTO unit;
	private List<IngredientComponentDTO> ingredients;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<SubRecipeComponentDTO> subRecipes;
}

