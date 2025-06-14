package com.bakery.costing.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RecipeCostDTO {
	private Long recipeId;
	private String recipeName;
	private BigDecimal costPerMainRecipe;
	private BigDecimal costPerSubRecipe;
	private BigDecimal totalCost;
	private BigDecimal retailPrice;
	private BigDecimal wholesalePrice;
}
