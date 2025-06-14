package com.bakery.recipe.service

import com.bakery.common.exception.RecipeNotFoundException
import com.bakery.ingredient.model.entity.Ingredient
import com.bakery.ingredient.repository.IngredientRepository
import com.bakery.recipe.mapper.RecipeMapper
import com.bakery.recipe.model.dto.*
import com.bakery.recipe.model.entity.*
import com.bakery.recipe.repository.RecipeRepository
import spock.lang.Specification

class RecipeServiceSpec extends Specification {

    def recipeRepo = Mock(RecipeRepository)
    def ingredientRepo = Mock(IngredientRepository)
    def mapper = Mock(RecipeMapper)
    def service = new RecipeService(recipeRepo, ingredientRepo, mapper)

    def "getAllRecipes returns mapped recipe DTOs with components"() {
        given:
        def recipe = new Recipe(id: 1L, name: "Cake", components: [])
        def dto = new RecipeDTO(id: 1L, name: "Cake")
        def ingList = [new IngredientComponentDTO()]
        def subList = [new SubRecipeComponentDTO()]

        when:
        recipeRepo.findAllWithComponents() >> [recipe]
        mapper.toDto(recipe) >> dto
        mapper.toIngredientComponents(recipe.components) >> ingList
        mapper.toSubRecipeComponents(recipe.components) >> subList

        def result = service.getAllRecipes()

        then:
        result.size() == 1
        result[0].ingredients == ingList
        result[0].subRecipes == subList
    }

    def "getRecipeById returns mapped recipe DTO or throws if not found"() {
        given:
        def recipe = new Recipe(id: 1L, components: [])
        def dto = new RecipeDTO(id: 1L)
        def ingList = [new IngredientComponentDTO()]
        def subList = [new SubRecipeComponentDTO()]

        when:
        recipeRepo.findByIdWithComponents(1L) >> Optional.of(recipe)
        mapper.toDto(recipe) >> dto
        mapper.toIngredientComponents(recipe.components) >> ingList
        mapper.toSubRecipeComponents(recipe.components) >> subList

        def result = service.getRecipeById(1L)

        then:
        result.id == 1L
        result.ingredients == ingList
        result.subRecipes == subList

        when:
        recipeRepo.findByIdWithComponents(2L) >> Optional.empty()
        service.getRecipeById(2L)

        then:
        thrown(RecipeNotFoundException)
    }

    def "saveRecipe saves new recipe with ingredients and sub-recipes"() {
        given:
        def dto = new RecipeDTO(name: "Cake", ingredients: [
                new IngredientComponentDTO(ingredientId: 10L, quantityUsed: 100)
        ], subRecipes: [
                new SubRecipeComponentDTO(subRecipeId: 20L, quantityUsed: 200)
        ])

        def entity = new Recipe(id: null, components: [])
        def saved = new Recipe(id: 1L, components: [])
        def mappedDto = new RecipeDTO(id: 1L)
        def ing = new Ingredient(id: 10L)
        def subRecipe = new Recipe(id: 20L, components: [])

        when:
        mapper.toEntity(dto) >> entity
        ingredientRepo.findById(10L) >> Optional.of(ing)
        recipeRepo.findByIdWithComponents(20L) >> Optional.of(subRecipe)
        recipeRepo.save(entity) >> saved
        mapper.toDto(saved) >> mappedDto
        mapper.toIngredientComponents(_) >> []
        mapper.toSubRecipeComponents(_) >> []

        def result = service.saveRecipe(dto)

        then:
        result.id == 1L
    }

    def "saveRecipe throws if ingredient or sub-recipe not found"() {
        given:
        def dto = new RecipeDTO(ingredients: [new IngredientComponentDTO(ingredientId: 999L)])
        mapper.toEntity(dto) >> new Recipe()

        when:
        ingredientRepo.findById(999L) >> Optional.empty()
        service.saveRecipe(dto)

        then:
        thrown(IllegalArgumentException)

        when:
        def dto2 = new RecipeDTO(subRecipes: [new SubRecipeComponentDTO(subRecipeId: 999L)])
        mapper.toEntity(dto2) >> new Recipe()
        recipeRepo.findByIdWithComponents(999L) >> Optional.empty()
        service.saveRecipe(dto2)

        then:
        thrown(IllegalArgumentException)
    }

    def "updateRecipe updates existing recipe and clears old components"() {
        given:
        def existing = new Recipe(id: 1L, components: [new RecipeComponent()])
        def dto = new RecipeDTO(name: "New", yieldAmount: 10, ingredients: [], subRecipes: [])
        def saved = new Recipe(id: 1L)
        def mappedDto = new RecipeDTO(id: 1L)

        recipeRepo.findByIdWithComponents(1L) >> Optional.of(existing)
        recipeRepo.save(existing) >> saved
        mapper.toDto(saved) >> mappedDto
        mapper.toIngredientComponents(_) >> []
        mapper.toSubRecipeComponents(_) >> []

        when:
        def result = service.updateRecipe(1L, dto)

        then:
        result.id == 1L
        existing.name == "New"
        existing.yieldAmount == 10
        existing.components.isEmpty()
    }

    def "updateRecipe throws if recipe not found"() {
        when:
        recipeRepo.findByIdWithComponents(1L) >> Optional.empty()
        service.updateRecipe(1L, new RecipeDTO())

        then:
        thrown(RecipeNotFoundException)
    }

    def "buildComponents throws on recursive cycle detection during update"() {
        given:
        def parentId = 1L
        def dto = new RecipeDTO(name: "Test", yieldAmount: 1.0, subRecipes: [new SubRecipeComponentDTO(subRecipeId: 2L)])
        def parent = new Recipe(id: parentId)
        def sub = new Recipe(id: 2L)
        def nested = new RecipeComponent(subRecipe: new Recipe(id: parentId)) // cycle
        sub.setComponents([nested] as Set)

        recipeRepo.findByIdWithComponents(parentId) >> Optional.of(parent)
        recipeRepo.findByIdWithComponents(2L) >> Optional.of(sub)

        when:
        service.updateRecipe(parentId, dto)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Sub-recipe would create a recursive cycle"
    }


    def "deleteRecipe calls deleteById"() {
        when:
        service.deleteRecipe(1L)

        then:
        1 * recipeRepo.deleteById(1L)
    }
}
