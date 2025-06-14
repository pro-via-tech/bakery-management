package com.bakery.recipe.mapper;

import com.bakery.recipe.model.dto.*;
import com.bakery.recipe.model.entity.*;
import com.bakery.unit.mapper.UnitMapper;
import com.bakery.unit.mapper.UnitMapperImpl;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface RecipeMapper {

	@Mapping(target = "ingredients", ignore = true)
	@Mapping(target = "subRecipes", ignore = true)
	@Mapping(target = "unit", source = "unit")  // Map unit in entity->dto
	RecipeDTO toDto(Recipe entity);

	@Mapping(target = "unit", source = "unit")  // Map unit in dto->entity
	@Mapping(target = "components", ignore = true)  // Avoid cycles, handle separately if needed
	Recipe toEntity(RecipeDTO dto);

	default List<IngredientComponentDTO> toIngredientComponents(Set<RecipeComponent> components) {
		return components.stream()
			.filter(RecipeComponent::isIngredient)
			.map(c -> {
				IngredientComponentDTO dto = new IngredientComponentDTO();
				dto.setIngredientId(c.getIngredient().getId());
				dto.setIngredientName(c.getIngredient().getName());
				dto.setQuantityUsed(c.getQuantityUsed());
				dto.setUnit(c.getIngredient().getUnit() != null ?
					new UnitMapperImpl().toDto(c.getIngredient().getUnit()) : null); // Map unit
				return dto;
			})
			.toList();
	}

	default List<SubRecipeComponentDTO> toSubRecipeComponents(Set<RecipeComponent> components) {
		return components.stream()
			.filter(RecipeComponent::isSubRecipe)
			.map(c -> {
				SubRecipeComponentDTO dto = new SubRecipeComponentDTO();
				Recipe sub = c.getSubRecipe();
				dto.setSubRecipeId(sub.getId());
				dto.setSubRecipeName(sub.getName());
				dto.setQuantityUsed(c.getQuantityUsed());
				dto.setUnit(sub.getUnit() != null ? new UnitMapperImpl().toDto(sub.getUnit()) : null); // Map unit

				List<IngredientComponentDTO> subIngredients = sub.getComponents().stream()
					.filter(RecipeComponent::isIngredient)
					.map(subComp -> {
						IngredientComponentDTO ic = new IngredientComponentDTO();
						ic.setIngredientId(subComp.getIngredient().getId());
						ic.setIngredientName(subComp.getIngredient().getName());
						ic.setQuantityUsed(subComp.getQuantityUsed());
						ic.setUnit(subComp.getIngredient().getUnit() != null ?
							new UnitMapperImpl().toDto(subComp.getIngredient().getUnit()) : null);
						return ic;
					}).collect(Collectors.toList());

				dto.setIngredients(subIngredients);
				return dto;
			}).collect(Collectors.toList());
	}
}
