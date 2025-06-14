package com.bakery.costing.service

import com.bakery.common.exception.RecipeNotFoundException
import com.bakery.costing.dto.RecipeCostDTO
import com.bakery.costing.mapper.RecipeCostMapper
import com.bakery.recipe.model.entity.Recipe
import com.bakery.recipe.model.entity.RecipeComponent
import com.bakery.recipe.repository.RecipeRepository
import spock.lang.Specification

class CostingServiceSpec extends Specification {

    def recipeRepository = Mock(RecipeRepository)
    def calculator = Mock(RecipeCostCalculator)
    def mapper = Mock(RecipeCostMapper)

    def service = new CostingService(recipeRepository, calculator, mapper)

    def setup() {
        service.retailMargin = new BigDecimal("0.30")
        service.wholesaleMargin = new BigDecimal("0.15")
    }

    def "calculateRecipeCost returns RecipeCostDTO correctly"() {
        given:
        def recipeId = 1L
        def recipe = new Recipe()
        def ingredientComponent = Mock(RecipeComponent) {
            isIngredient() >> true
        }
        def subRecipeComponent = Mock(RecipeComponent) {
            isIngredient() >> false
        }

        recipe.setComponents([ingredientComponent, subRecipeComponent] as Set)

        def mainCost = new BigDecimal("8.00")
        def totalCost = new BigDecimal("10.00")
        def retailPrice = new BigDecimal("13.00")
        def wholesalePrice = new BigDecimal("11.50")
        def dto = new RecipeCostDTO()

        when:
        recipeRepository.findByIdWithComponents(recipeId) >> Optional.of(recipe)
        calculator.calculateTotalCost(recipe) >> totalCost
        calculator.calculateComponentCost(ingredientComponent) >> mainCost
        mapper.toDto(recipe, mainCost, totalCost.subtract(mainCost), totalCost, retailPrice, wholesalePrice) >> dto

        def result = service.calculateRecipeCost(recipeId)

        then:
        result == dto
    }

    def "calculateRecipeCost throws RecipeNotFoundException when recipe not found"() {
        given:
        def recipeId = 99L
        recipeRepository.findByIdWithComponents(recipeId) >> Optional.empty()

        when:
        service.calculateRecipeCost(recipeId)

        then:
        thrown(RecipeNotFoundException)
    }

    def "applyMargin calculates correctly"() {
        expect:
        service.applyMargin(new BigDecimal("100.00"), new BigDecimal("0.25")) == new BigDecimal("125.00")
        service.applyMargin(new BigDecimal("80.00"), new BigDecimal("0.10")) == new BigDecimal("88.00")
    }

    def "calculateMainCostOnly handles empty component list"() {
        given:
        def recipe = new Recipe()
        recipe.setComponents([] as Set)

        when:
        def result = service.calculateMainCostOnly(recipe)

        then:
        result == BigDecimal.ZERO.setScale(2)
    }

    def "calculateMainCostOnly sums only ingredient components"() {
        given:
        def recipe = new Recipe()
        def comp1 = Mock(RecipeComponent) {
            isIngredient() >> true
        }
        def comp2 = Mock(RecipeComponent) {
            isIngredient() >> false
        }
        def comp3 = Mock(RecipeComponent) {
            isIngredient() >> true
        }

        recipe.setComponents([comp1, comp2, comp3] as Set)

        when:
        calculator.calculateComponentCost(comp1) >> new BigDecimal("3.00")
        calculator.calculateComponentCost(comp3) >> new BigDecimal("2.00")

        def result = service.calculateMainCostOnly(recipe)

        then:
        result == new BigDecimal("5.00").setScale(2)
    }
}

