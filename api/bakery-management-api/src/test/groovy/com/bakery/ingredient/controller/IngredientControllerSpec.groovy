package com.bakery.ingredient.controller

import com.bakery.common.exception.GlobalExceptionHandler
import com.bakery.ingredient.model.dto.IngredientDTO
import com.bakery.ingredient.model.dto.IngredientListDTO
import com.bakery.ingredient.model.dto.NutritionSummaryDTO
import com.bakery.ingredient.service.IngredientService
import com.bakery.unit.model.dto.UnitDTO
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(controllers = IngredientController)
class IngredientControllerSpec extends Specification {

    MockMvc mvc
    ObjectMapper objectMapper = new ObjectMapper()
    IngredientService ingredientService = Mock(IngredientService.class)

    def setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new IngredientController(ingredientService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build()
    }

    def "GET /api/ingredients returns all ingredients"() {
        given:
        def ingredients = [
                new IngredientDTO(id: 1L, name: "Flour", unit: new UnitDTO(name: "kg")),
                new IngredientDTO(id: 2L, name: "Sugar", unit: new UnitDTO(name: "g"))
        ]
        ingredientService.getAllIngredients() >> ingredients

        expect:
        mvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.success', is(true)))
                .andExpect(jsonPath('$.data', hasSize(2)))
    }

    def "GET /api/ingredients/{id} returns ingredient by ID"() {
        given:
        def dto = new IngredientDTO(id: 1L, name: "Salt", unit: new UnitDTO(name: "g"))
        ingredientService.getIngredientById(1L) >> dto

        expect:
        mvc.perform(get("/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.name', is("Salt")))
    }

    def "POST /api/ingredients creates ingredients when no IDs are present"() {
        given:
        def input = [
                new IngredientDTO(name: "Butter", purchaseCost: 10, purchaseQuantity: 1, unit: new UnitDTO(name: "g"), supplier: "Costco", nutritionSummary: new NutritionSummaryDTO(3, 9, 6))
        ]
        def saved = [
                new IngredientDTO(id: 100L, name: "Butter", purchaseCost: 10, purchaseQuantity: 1, unit: new UnitDTO(name: "g"))
        ]
        ingredientService.saveIngredient(_) >> saved[0]

        expect:
        mvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new IngredientListDTO(ingredients: input))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('$.data.ingredients[0].id', is(100)))
    }

    def "POST /api/ingredients throws if any DTO has ID set"() {
        given:
        def input = [
                new IngredientDTO(id: 99L, name: "Oil", unit: new UnitDTO(name: "ml"))
        ]

        expect:
        mvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new IngredientListDTO(ingredients: input))))
                .andExpect(status().isBadRequest())
    }

    def "PUT /api/ingredients/{id} updates ingredient when ID matches"() {
        given:
        def input = new IngredientDTO(id: 1L, name: "Updated Flour", purchaseCost: 10, purchaseQuantity: 1, unit: new UnitDTO(name: "kg"), supplier: "Costco", nutritionSummary: new NutritionSummaryDTO(3, 9, 6))
        def updated = new IngredientDTO(id: 1L, name: "Updated Flour", unit: new UnitDTO(name: "kg"), supplier: "Costco", nutritionSummary: new NutritionSummaryDTO(3, 9, 6))
        ingredientService.updateIngredient(*_) >> updated

        expect:
        mvc.perform(put("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.name', is("Updated Flour")))
    }

    def "PUT /api/ingredients/{id} throws when DTO ID doesn't match path"() {
        given:
        def input = new IngredientDTO(id: 2L, name: "Mismatch")

        expect:
        mvc.perform(put("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
    }

    def "DELETE /api/ingredients/{id} removes the ingredient"() {
        expect:
        mvc.perform(delete("/api/ingredients/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.message', containsString("deleted")))
    }
}

