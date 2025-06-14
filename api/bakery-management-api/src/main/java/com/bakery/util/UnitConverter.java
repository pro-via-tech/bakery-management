package com.bakery.util;

import com.bakery.ingredient.model.entity.Ingredient;
import com.bakery.unit.model.entity.Unit;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ZERO;

public class UnitConverter {

	public static BigDecimal toGrams(BigDecimal amount, Unit unit) {
		if (unit == null || unit.getConversionToGram() == null || unit.getConversionToGram().compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException("Invalid unit or conversion factor.");
		}
		return amount.multiply(unit.getConversionToGram());
	}

	public static BigDecimal calculateCostPerGram(Ingredient ingredient) {
		if (ingredient == null || ingredient.getPurchaseCost() == null || ingredient.getPurchaseQuantity() == null || ingredient.getPurchaseQuantity() == 0) {
			return ZERO;
		}

		BigDecimal costPerUnit = ingredient.getPurchaseCost().divide(BigDecimal.valueOf(ingredient.getPurchaseQuantity()), 2, RoundingMode.HALF_UP);
		BigDecimal gramsPerUnit = ingredient.getUnitWeightInGrams() != null
			? ingredient.getUnitWeightInGrams()
			: ingredient.getUnit().getConversionToGram();

		return gramsPerUnit.compareTo(ZERO) == 0 ? ZERO : costPerUnit.divide(gramsPerUnit, 2, RoundingMode.HALF_UP);
	}

	public static BigDecimal calculateCostPerUnit(Ingredient ingredient) {
		return ingredient.getPurchaseCost()
			.divide(BigDecimal.valueOf(ingredient.getPurchaseQuantity()), 2, RoundingMode.HALF_UP);
	}
}
