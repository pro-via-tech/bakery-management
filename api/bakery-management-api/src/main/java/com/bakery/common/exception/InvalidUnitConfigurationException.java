package com.bakery.common.exception;

public class InvalidUnitConfigurationException extends RuntimeException {
	public InvalidUnitConfigurationException(String ingredientName, String unitName) {
		super("Ingredient " + ingredientName + " with unit type " + unitName + " must have a valid unitWeightInGrams.");
	}
}
