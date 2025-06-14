package com.bakery.ingredient.mapper;

import com.bakery.ingredient.model.dto.IngredientDTO;
import com.bakery.ingredient.model.entity.Ingredient;
import com.bakery.unit.mapper.UnitMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {NutritionSummaryMapper.class, UnitMapper.class})
public interface IngredientMapper {

	@Mapping(target = "nutritionSummary", source = "nutritionSummary", qualifiedByName = "mapToEntity")
	Ingredient toEntity(IngredientDTO dto);

//	@Mapping(target = "costPerUnit", expression = "java(com.bakery.util.UnitConverter.calculateCostPerUnit(entity))")
//	@Mapping(target = "costPerGram", expression = "java(com.bakery.util.UnitConverter.calculateCostPerGram(entity))")
//	@Mapping(target = "purchaseUnit", source = "purchaseUnit")  // Add this line to map Unit entity -> UnitDTO
	IngredientDTO toDto(Ingredient entity);

	@Mapping(target = "nutritionSummary", source = "nutritionSummary", qualifiedByName = "mapToEntity")
//	@Mapping(target = "purchaseUnit", source = "purchaseUnit")  // Map unit in update too
	void updateEntityFromDto(IngredientDTO dto, @MappingTarget Ingredient entity);
}
