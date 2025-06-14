package com.bakery.ingredient.model.entity;

import com.bakery.unit.model.entity.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ingredients")
public class Ingredient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Ingredient name is required")
	private String name;

	@NotNull(message = "Purchase cost is required")
	private BigDecimal purchaseCost;

	@NotNull(message = "Purchase quantity is required")
	private Double purchaseQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", nullable = false)
	@NotNull(message = "Unit is required")
	private Unit unit;

	@NotBlank(message = "Supplier is required")
	private String supplier;

	@Embedded
	private NutritionSummary nutritionSummary;

	/**
	 * For "UNIT"-based ingredients, this value defines how much a single unit weighs in grams.
	 * For others, it can be null.
	 */
	private BigDecimal unitWeightInGrams;


	private BigDecimal costPerUnit;
	private BigDecimal costPerGram;

}

