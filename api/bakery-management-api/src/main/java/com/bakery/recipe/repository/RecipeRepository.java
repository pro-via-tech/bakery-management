package com.bakery.recipe.repository;

import com.bakery.recipe.model.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	@Query("""
    SELECT DISTINCT r FROM Recipe r
    LEFT JOIN FETCH r.unit ru
    LEFT JOIN FETCH r.components c
    LEFT JOIN FETCH c.ingredient i
    LEFT JOIN FETCH i.unit
    LEFT JOIN FETCH c.subRecipe sr
    LEFT JOIN FETCH sr.unit
    LEFT JOIN FETCH sr.components src
    LEFT JOIN FETCH src.ingredient sri
    LEFT JOIN FETCH sri.unit
    WHERE r.id = :id
    """)
	Optional<Recipe> findByIdWithComponents(@Param("id") Long id);



	@Query("""
	SELECT DISTINCT r FROM Recipe r
	LEFT JOIN FETCH r.unit u
	LEFT JOIN FETCH r.components c
	LEFT JOIN FETCH c.ingredient i
	LEFT JOIN FETCH i.unit
	LEFT JOIN FETCH c.subRecipe sr
	LEFT JOIN FETCH sr.unit
	LEFT JOIN FETCH sr.components src
	LEFT JOIN FETCH src.ingredient si
	LEFT JOIN FETCH si.unit
	""")
	List<Recipe> findAllWithComponents();

}
