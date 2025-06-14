package com.bakery.ingredient.mapper;

import com.bakery.ingredient.model.dto.NutritionSummaryDTO;
import com.bakery.ingredient.model.entity.NutritionSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NutritionSummaryMapper {

	@Named("mapToEntity")
	default NutritionSummary toEntity(NutritionSummaryDTO dto) {
		if (dto == null) {
			return null;
		}
		return new NutritionSummary(dto.getProtein(), dto.getCarbs(), dto.getFat());
	}

	@Named("mapToDto")
	default NutritionSummaryDTO toDto(NutritionSummary entity) {
		if (entity == null) return null;
		return NutritionSummaryDTO.builder()
			.protein(entity.getProtein())
			.carbs(entity.getCarbs())
			.fat(entity.getFat())
			.build();
	}
}
