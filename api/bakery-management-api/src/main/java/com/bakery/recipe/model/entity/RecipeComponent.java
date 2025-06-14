package com.bakery.recipe.model.entity;

import com.bakery.ingredient.model.entity.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_components")
@Getter
@Setter
public class RecipeComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id", nullable = false)
	private Recipe recipe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingredient_id")
	private Ingredient ingredient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_recipe_id")
	private Recipe subRecipe;

	@NotNull
	private Double quantityUsed;

	public boolean isIngredient() {
		return ingredient != null;
	}

	public boolean isSubRecipe() {
		return subRecipe != null;
	}
}

