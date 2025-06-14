package com.bakery.recipe.service;

import com.bakery.common.exception.RecipeNotFoundException;
import com.bakery.ingredient.model.entity.Ingredient;
import com.bakery.ingredient.repository.IngredientRepository;
import com.bakery.recipe.mapper.RecipeMapper;
import com.bakery.recipe.model.dto.IngredientComponentDTO;
import com.bakery.recipe.model.dto.RecipeDTO;
import com.bakery.recipe.model.dto.SubRecipeComponentDTO;
import com.bakery.recipe.model.entity.Recipe;
import com.bakery.recipe.model.entity.RecipeComponent;
import com.bakery.recipe.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final RecipeMapper mapper;

	public List<RecipeDTO> getAllRecipes() {
		return recipeRepository.findAllWithComponents().stream()
			.map(recipe -> {
				RecipeDTO dto = mapper.toDto(recipe);
				dto.setIngredients(mapper.toIngredientComponents(recipe.getComponents()));
				dto.setSubRecipes(mapper.toSubRecipeComponents(recipe.getComponents()));
				return dto;
			})
			.toList();
	}


	public RecipeDTO getRecipeById(Long id) {
		Recipe recipe = recipeRepository.findByIdWithComponents(id)
			.orElseThrow(() -> new RecipeNotFoundException(id));

		RecipeDTO dto = mapper.toDto(recipe);
		dto.setIngredients(mapper.toIngredientComponents(recipe.getComponents()));
		dto.setSubRecipes(mapper.toSubRecipeComponents(recipe.getComponents()));
		return dto;
	}


	@Transactional
	public RecipeDTO saveRecipe(RecipeDTO dto) {
		Recipe recipe = mapper.toEntity(dto);

		Set<RecipeComponent> components = buildComponents(dto, recipe, null);
		recipe.setComponents(components);

		Recipe saved = recipeRepository.save(recipe);

		RecipeDTO result = mapper.toDto(saved);
		result.setIngredients(mapper.toIngredientComponents(saved.getComponents()));
		result.setSubRecipes(mapper.toSubRecipeComponents(saved.getComponents()));
		return result;
	}

	@Transactional
	public RecipeDTO updateRecipe(Long id, RecipeDTO dto) {
		Recipe recipe = recipeRepository.findByIdWithComponents(id)
			.orElseThrow(() -> new RecipeNotFoundException(id));

		recipe.setName(dto.getName());
		recipe.setYieldAmount(dto.getYieldAmount());
		recipe.getComponents().clear();

		Set<RecipeComponent> components = buildComponents(dto, recipe, id);
		recipe.setComponents(components);

		Recipe saved = recipeRepository.save(recipe);

		RecipeDTO result = mapper.toDto(saved);
		result.setIngredients(mapper.toIngredientComponents(saved.getComponents()));
		result.setSubRecipes(mapper.toSubRecipeComponents(saved.getComponents()));
		return result;
	}


	private Set<RecipeComponent> buildComponents(RecipeDTO dto, Recipe parent, Long parentId) {
		Set<RecipeComponent> components = new HashSet<>();

		if (dto.getIngredients() != null) {
			for (IngredientComponentDTO ic : dto.getIngredients()) {
				Ingredient ingredient = ingredientRepository.findById(ic.getIngredientId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid ingredient ID: " + ic.getIngredientId()));

				RecipeComponent component = new RecipeComponent();
				component.setRecipe(parent);
				component.setIngredient(ingredient);
				component.setQuantityUsed(ic.getQuantityUsed());
				components.add(component);
			}
		}

		if (dto.getSubRecipes() != null) {
			for (SubRecipeComponentDTO sc : dto.getSubRecipes()) {
				Recipe subRecipe = recipeRepository.findByIdWithComponents(sc.getSubRecipeId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid sub-recipe ID: " + sc.getSubRecipeId()));

				// Optional: Cycle detection
				if (isCycle(parentId, subRecipe)) {
					throw new IllegalArgumentException("Sub-recipe would create a recursive cycle");
				}

				RecipeComponent component = new RecipeComponent();
				component.setRecipe(parent);
				component.setSubRecipe(subRecipe);
				component.setQuantityUsed(sc.getQuantityUsed());
				components.add(component);
			}
		}

		return components;
	}

	private boolean isCycle(Long parentId, Recipe subRecipe) {
		if (parentId == null || subRecipe == null) return false;
		Set<Long> visited = new HashSet<>();
		return hasCycle(subRecipe, parentId, visited);
	}

	private boolean hasCycle(Recipe current, Long targetId, Set<Long> visited) {
		if (current.getId() == null) return false;
		if (current.getId().equals(targetId)) return true;
		if (!visited.add(current.getId())) return false; // already visited

		for (RecipeComponent c : current.getComponents()) {
			if (c.isSubRecipe() && c.getSubRecipe() != null) {
				if (hasCycle(c.getSubRecipe(), targetId, visited)) return true;
			}
		}
		return false;
	}


	public void deleteRecipe(Long id) {
		recipeRepository.deleteById(id);
	}
}



