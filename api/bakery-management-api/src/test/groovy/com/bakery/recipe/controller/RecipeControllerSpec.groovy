package com.bakery.recipe.controller

import com.bakery.common.exception.GlobalExceptionHandler
import com.bakery.recipe.model.dto.RecipeDTO
import com.bakery.recipe.service.RecipeService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = RecipeController)
class RecipeControllerSpec extends Specification {

    MockMvc mvc
    ObjectMapper objectMapper = new ObjectMapper()
    RecipeService recipeService = Mock(RecipeService.class)

    def setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new RecipeController(recipeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build()
    }

    def "GET /api/recipes returns all recipes"() {
        given:
        def recipes = [
                new RecipeDTO(id: 1L, name: "Chocolate Cake"),
                new RecipeDTO(id: 2L, name: "Banana Bread")
        ]

        recipeService.getAllRecipes() >> recipes

        expect:
        mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.success', is(true)))
                .andExpect(jsonPath('$.data', hasSize(2)))
    }

    def "GET /api/recipes/{id} returns recipe by ID"() {
        given:
        def recipe = new RecipeDTO(id: 1L, name: "Pancakes")
        recipeService.getRecipeById(1L) >> recipe

        expect:
        mvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.name', is("Pancakes")))
    }

    def "POST /api/recipes creates a recipe"() {
        given:
        def input = new RecipeDTO(name: "Muffins")
        def saved = new RecipeDTO(id: 5L, name: "Muffins")

        recipeService.saveRecipe(_ as RecipeDTO) >> saved

        expect:
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.id', is(5)))
                .andExpect(jsonPath('$.data.name', is("Muffins")))
    }

    def "PUT /api/recipes/{id} updates a recipe"() {
        given:
        def input = new RecipeDTO(id: 1L, name: "Updated Muffins")
        def updated = new RecipeDTO(id: 1L, name: "Updated Muffins")

        recipeService.updateRecipe(_ as Long, _ as RecipeDTO) >> updated

        expect:
        mvc.perform(put("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.name', is("Updated Muffins")))
    }

    def "DELETE /api/recipes/{id} deletes a recipe"() {
        expect:
        mvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.message', containsString("deleted")))
    }
}
