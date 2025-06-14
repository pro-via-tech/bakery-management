package com.bakery.costing.service

import com.bakery.recipe.model.entity.Recipe
import com.bakery.recipe.model.entity.RecipeComponent
import com.bakery.ingredient.model.entity.Ingredient
import com.bakery.unit.model.entity.Unit
import spock.lang.Specification

class RecipeCostCalculatorSpec extends Specification {

    def calculator = new RecipeCostCalculator()

    def "calculateTotalCost should sum all component costs"() {
        given:
        def flour = ingredient("Flour", 0.0023)
        def butter = ingredient("Butter", 0.0129)

        def comp1 = ingredientComponent(flour, 1000) // 1000g
        def comp2 = ingredientComponent(butter, 200) // 200g

        def recipe = new Recipe(components: [comp1, comp2] as Set)

        expect:
        calculator.calculateTotalCost(recipe) == 4.88
    }

    def "calculateComponentCost should compute cost for ingredient"() {
        given:
        def sugar = ingredient("Sugar", 0.003)
        def component = ingredientComponent(sugar, 500) // 500g

        expect:
        calculator.calculateComponentCost(component) == 1.50
    }

    def "calculateComponentCost should compute cost for sub-recipe"() {
        given:
        def egg = ingredient("Egg", 0.0071)
        def subComp = ingredientComponent(egg, 100)
        def subRecipe = new Recipe(components: [subComp] as Set)

        def parentComp = new RecipeComponent(
                subRecipe: subRecipe,
                quantityUsed: 2.0
        )

        expect:
        calculator.calculateComponentCost(parentComp) == 1.42 // 0.71 * 2
    }

    def "calculateIngredientCost should throw if unit is null"() {
        given:
        def ingredient = new Ingredient(name: "No Unit", costPerGram: BigDecimal.valueOf(0.01), unit: null)
        def component = new RecipeComponent(ingredient: ingredient, quantityUsed: 1.0)

        when:
        calculator.calculateIngredientCost(component)

        then:
        thrown(NullPointerException)
    }

    def "calculateIngredientCost should return zero if quantityUsed is zero"() {
        given:
        def cocoa = ingredient("Cocoa", 0.009)
        def component = ingredientComponent(cocoa, 0)

        expect:
        calculator.calculateIngredientCost(component) == 0.00
    }

    // === Helper Methods ===

    private Ingredient ingredient(String name, double costPerGram) {
        return new Ingredient(
                name: name,
                costPerGram: BigDecimal.valueOf(costPerGram),
                unit: new Unit(name: "GRAM", displayName: "g", conversionToGram: BigDecimal.ONE)
        )
    }

    private RecipeComponent ingredientComponent(Ingredient ingredient, double gramsUsed) {
        return new RecipeComponent(
                ingredient: ingredient,
                quantityUsed: gramsUsed
        )
    }
}

