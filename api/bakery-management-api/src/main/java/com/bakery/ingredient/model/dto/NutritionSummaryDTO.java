package com.bakery.ingredient.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionSummaryDTO {

	@NotNull
	@Positive(message = "Protein must be positive")
	private Integer protein;

	@NotNull
	@Positive(message = "Carbs must be positive")
	private Integer carbs;

	@NotNull
	@Positive(message = "Fat must be positive")
	private Integer fat;

	// calories NOT here — it’s calculated internally, never client-supplied
}

