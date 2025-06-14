package com.bakery.unit.model.entity;

import com.bakery.unit.model.enums.UnitScope;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;  // KILOGRAM, GRAM, etc.

	@Column(nullable = false)
	private String displayName;  // kg, g, etc.

	@Column(nullable = false)
	private BigDecimal conversionToGram;  // e.g., 1000 for kg

	@NotNull
	@Enumerated(EnumType.STRING)
	private UnitScope type; // INGREDIENT, RECIPE, BOTH

	@Column(name = "is_discrete_unit")
	private boolean discrete;
}
