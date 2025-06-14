package com.bakery.costing.mapper;

import com.bakery.recipe.model.entity.Recipe;
import com.bakery.costing.dto.RecipeCostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface RecipeCostMapper {

	@Mapping(source = "recipe.id", target = "recipeId")
	@Mapping(source = "recipe.name", target = "recipeName")
	@Mapping(source = "mainCost", target = "costPerMainRecipe")
	@Mapping(source = "subRecipeCost", target = "costPerSubRecipe")
	@Mapping(source = "totalCost", target = "totalCost")
	@Mapping(source = "retailPrice", target = "retailPrice")
	@Mapping(source = "wholesalePrice", target = "wholesalePrice")
	RecipeCostDTO toDto(
		Recipe recipe,
		BigDecimal mainCost,
		BigDecimal subRecipeCost,
		BigDecimal totalCost,
		BigDecimal retailPrice,
		BigDecimal wholesalePrice
	);
}
