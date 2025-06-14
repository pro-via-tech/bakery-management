package com.bakery.ingredient.model.dto;

import com.bakery.unit.model.dto.UnitDTO;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {

	private Long id;

	@NotBlank(message = "Name must not be blank")
	private String name;

	@NotNull(message = "Purchase cost must not be blank")
	@Positive(message = "Purchase cost must be positive")
	private BigDecimal purchaseCost;

	@NotNull(message = "Purchase quantity must not be null")
	@Positive(message = "Purchase quantity must be positive")
	private Double purchaseQuantity;

	@NotNull(message = "Purchase unit must not be null")
	private UnitDTO unit;

	@NotBlank(message = "Supplier must not be blank")
	private String supplier;

	@NotNull(message = "Nutrition summary must not be null")
	private NutritionSummaryDTO nutritionSummary;

	private Double unitWeightInGrams;
//	private Double gramsPerUnit;
	private BigDecimal costPerUnit;
//	private BigDecimal costPerGram;

}
