package com.bakery.costing.service;

import com.bakery.common.exception.RecipeNotFoundException;
import com.bakery.recipe.model.entity.Recipe;
import com.bakery.recipe.model.entity.RecipeComponent;
import com.bakery.recipe.repository.RecipeRepository;
import com.bakery.costing.dto.RecipeCostDTO;
import com.bakery.costing.mapper.RecipeCostMapper;
import com.bakery.util.UnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CostingService {

	private final RecipeRepository recipeRepository;
	private final RecipeCostCalculator calculator;
	private final RecipeCostMapper mapper;

	@Value("${app.costing.margin.retail}")
	private BigDecimal retailMargin;

	@Value("${app.costing.margin.wholesale}")
	private BigDecimal wholesaleMargin;

	private static final int SCALE = 2;

	public RecipeCostDTO calculateRecipeCost(Long recipeId) {
		Recipe recipe = recipeRepository.findByIdWithComponents(recipeId)
			.orElseThrow(() -> new RecipeNotFoundException(recipeId));

		BigDecimal totalCost = calculator.calculateTotalCost(recipe);
		BigDecimal retailPrice = applyMargin(totalCost, retailMargin);
		BigDecimal wholesalePrice = applyMargin(totalCost, wholesaleMargin);

		// Optional: distinguish cost between direct vs nested, if needed
		BigDecimal mainCost = calculateMainCostOnly(recipe);
		BigDecimal subRecipeCost = totalCost.subtract(mainCost);

		return mapper.toDto(recipe, mainCost, subRecipeCost, totalCost, retailPrice, wholesalePrice);
	}

	private BigDecimal calculateMainCostOnly(Recipe recipe) {
		return recipe.getComponents().stream()
			.filter(RecipeComponent::isIngredient)
			.map(calculator::calculateComponentCost)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(SCALE, RoundingMode.HALF_UP);
	}

	private BigDecimal applyMargin(BigDecimal base, BigDecimal margin) {
		return base.multiply(margin.add(BigDecimal.ONE)).setScale(SCALE, RoundingMode.HALF_UP);
	}
}