package com.bakery.ingredient.service;

import com.bakery.common.exception.IngredientNotFoundException;
import com.bakery.common.exception.InvalidUnitConfigurationException;
import com.bakery.ingredient.mapper.IngredientMapper;
import com.bakery.ingredient.model.dto.IngredientDTO;
import com.bakery.ingredient.model.entity.Ingredient;
import com.bakery.ingredient.repository.IngredientRepository;
import com.bakery.unit.mapper.UnitMapper;
import com.bakery.unit.model.dto.UnitDTO;
import com.bakery.unit.model.entity.Unit;
import com.bakery.unit.model.enums.UnitScope;
import com.bakery.unit.service.UnitService;
import com.bakery.util.UnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
public class IngredientService {

	private final IngredientRepository ingredientRepository;
	private final IngredientMapper ingredientMapper;
	private final UnitService unitService;
	private final UnitMapper unitMapper;

	public List<IngredientDTO> getAllIngredients() {
		return ingredientRepository.findAllWithUnit()
			.stream()
			.map(ingredientMapper::toDto)
			.toList();
	}

	public IngredientDTO getIngredientById(Long id) {
		Ingredient ingredient = ingredientRepository.findByIdWithUnit(id)
			.orElseThrow(() -> new IngredientNotFoundException(id));
		return ingredientMapper.toDto(ingredient);
//		return mapWithCalculations(ingredient);
	}

	public IngredientDTO saveIngredient(IngredientDTO dto) {
		Unit unit = unitService.validateOrNormalize(dto.getUnit());
		UnitDTO unitDTO = unitMapper.toDto(unit);
		dto.setUnit(unitDTO);
		Ingredient ingredient = ingredientMapper.toEntity(dto);
		validateUnitConfiguration(ingredient);

		calculateAndSetDerivedValues(ingredient);

		Ingredient saved = ingredientRepository.save(ingredient);
//		return mapWithCalculations(saved);
		return ingredientMapper.toDto(saved);
	}

	public IngredientDTO updateIngredient(Long id, IngredientDTO dto) {
		Ingredient existing = ingredientRepository.findByIdWithUnit(id)
			.orElseThrow(() -> new IngredientNotFoundException(id));

		Unit unit = unitService.validateOrNormalize(dto.getUnit());
		UnitDTO unitDTO = unitMapper.toDto(unit);
		dto.setUnit(unitDTO);

		ingredientMapper.updateEntityFromDto(dto, existing);
		validateUnitConfiguration(existing);

		if (!"UNIT".equalsIgnoreCase(existing.getUnit().getName())) {
			BigDecimal gramsPerUnit = UnitConverter.toGrams(ONE, existing.getUnit());
			existing.setUnitWeightInGrams(gramsPerUnit);
		}

		Ingredient updated = ingredientRepository.save(existing);
//		return mapWithCalculations(updated);
		return ingredientMapper.toDto(updated);
	}

	public void deleteIngredient(Long id) {
		if (!ingredientRepository.existsById(id)) {
			throw new IngredientNotFoundException(id);
		}
		ingredientRepository.deleteById(id);
	}

	private void calculateAndSetDerivedValues(Ingredient ingredient) {
		BigDecimal costPerUnit = UnitConverter.calculateCostPerUnit(ingredient);
		if (ingredient.getUnit().getType() == UnitScope.RECIPE) {
			ingredient.setCostPerUnit(costPerUnit);
			return;
		}

		BigDecimal gramsPerUnit;

		if (ingredient.getUnit().isDiscrete()) {
			// "Discrete" means a single unit has a known weight in grams
			gramsPerUnit = ingredient.getUnitWeightInGrams(); // must be provided via DTO or elsewhere
			if (gramsPerUnit.compareTo(ZERO) <= 0) {
				throw new IllegalArgumentException("Grams per unit must be > 0 for discrete units.");
			}
			ingredient.setUnitWeightInGrams(gramsPerUnit); // ensure it’s stored
		} else {
			// For mass/volume units (e.g., kg, g, lb) we calculate it from standard conversion
			gramsPerUnit = UnitConverter.toGrams(ONE, ingredient.getUnit());
			ingredient.setUnitWeightInGrams(null); // remove accidental data
		}

		// Calculate costPerGram safely
		BigDecimal costPerGram = (gramsPerUnit.compareTo(ZERO) == 0)
			? ZERO
			: costPerUnit.divide(gramsPerUnit, 2, RoundingMode.HALF_UP);

		ingredient.setCostPerUnit(costPerUnit);
		ingredient.setCostPerGram(costPerGram);
	}

	private BigDecimal gramsPerUnitOrDefault(Ingredient ingredient) {
		return ingredient.getUnitWeightInGrams() != null ? ingredient.getUnitWeightInGrams() : ONE;
	}

	private void validateUnitConfiguration(Ingredient ingredient) {
		if (ingredient.getUnit().isDiscrete() &&
			(ingredient.getUnitWeightInGrams() == null || ingredient.getUnitWeightInGrams().compareTo(ZERO) <= 0)) {
			throw new InvalidUnitConfigurationException(ingredient.getName(), ingredient.getUnit().getName());
		}
	}
}
