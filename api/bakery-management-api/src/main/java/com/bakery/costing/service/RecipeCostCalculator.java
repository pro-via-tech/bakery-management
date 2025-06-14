package com.bakery.costing.service;

import com.bakery.recipe.model.entity.Recipe;
import com.bakery.recipe.model.entity.RecipeComponent;
import com.bakery.unit.model.entity.Unit;
import com.bakery.util.UnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RecipeCostCalculator {

	private static final int TWO_DECIMALS = 2;

	public BigDecimal calculateTotalCost(Recipe recipe) {
		return recipe.getComponents().stream()
			.map(this::calculateComponentCost)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(TWO_DECIMALS, RoundingMode.HALF_UP);
	}

	public BigDecimal calculateComponentCost (RecipeComponent comp) {
		if (comp.isIngredient()) {
			return calculateIngredientCost(comp);
		} else if (comp.isSubRecipe()) {
			BigDecimal subCost = calculateTotalCost(comp.getSubRecipe());
			return subCost.multiply(BigDecimal.valueOf(comp.getQuantityUsed()));
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calculateIngredientCost(RecipeComponent comp) {
		BigDecimal quantity = BigDecimal.valueOf(comp.getQuantityUsed());
		Unit unit = Objects.requireNonNull(comp.getIngredient().getUnit(), "Unit must not be null");
		BigDecimal grams = UnitConverter.toGrams(quantity, unit);
		BigDecimal costPerGram = comp.getIngredient().getCostPerGram();

		return grams.multiply(costPerGram);
	}
}

