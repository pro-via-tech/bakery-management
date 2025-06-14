package com.bakery.recipe.model.entity;

import com.bakery.unit.model.entity.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@NotNull
	private Double yieldAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id")
	private Unit unit;

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RecipeComponent> components = new HashSet<>();
}

