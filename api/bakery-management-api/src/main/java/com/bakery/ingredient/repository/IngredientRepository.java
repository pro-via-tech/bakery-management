package com.bakery.ingredient.repository;


import com.bakery.ingredient.model.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	@Query("SELECT i FROM Ingredient i JOIN FETCH i.unit")
	List<Ingredient> findAllWithUnit();

	@Query("SELECT i FROM Ingredient i JOIN FETCH i.unit WHERE i.id = :id")
	Optional<Ingredient> findByIdWithUnit(@Param("id") Long id);

}
