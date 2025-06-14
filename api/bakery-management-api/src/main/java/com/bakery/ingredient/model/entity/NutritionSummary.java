package com.bakery.ingredient.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@Access(AccessType.FIELD)  // Use field access to allow JPA to set fields directly
public class NutritionSummary {

	private final Integer protein;
	private final Integer carbs;
	private final Integer fat;
	private final Integer calories;

	// Required by JPA — protected no-arg constructor
	protected NutritionSummary() {
		this.protein = 0;
		this.carbs = 0;
		this.fat = 0;
		this.calories = 0;
	}

	// Main constructor calculating calories internally
	public NutritionSummary(Integer protein, Integer carbs, Integer fat) {
		this.protein = protein != null ? protein : 0;
		this.carbs = carbs != null ? carbs : 0;
		this.fat = fat != null ? fat : 0;
		this.calories = calculateCalories(this.protein, this.carbs, this.fat);
	}

	// Factory method for convenience
	public static NutritionSummary of(int protein, int carbs, int fat) {
		return new NutritionSummary(protein, carbs, fat);
	}

	private static int calculateCalories(int protein, int carbs, int fat) {
		return 4 * protein + 4 * carbs + 9 * fat;
	}
}

