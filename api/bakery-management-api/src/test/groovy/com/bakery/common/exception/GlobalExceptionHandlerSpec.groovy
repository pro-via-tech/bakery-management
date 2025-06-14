package com.bakery.common.exception

import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GlobalExceptionHandlerSpec extends Specification {

    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new FakeController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build()
    }

    def "handles IdNotAllowedException"() {
        expect:
        mvc.perform(get("/fake/id-not-allowed"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath('$.message').value("ID must not be provided for this operation."))
                .andExpect(MockMvcResultMatchers.jsonPath('$.errors[0].code').value("ID NOT ALLOWED"))
    }

    def "handles IngredientNotFoundException"() {
        expect:
        mvc.perform(get("/fake/ingredient-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath('$.errors[0].code').value("INGREDIENT_NOT_FOUND"))
    }

    def "handles RecipeNotFoundException"() {
        expect:
        mvc.perform(get("/fake/recipe-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath('$.errors[0].code').value("RECIPE_NOT_FOUND"))
    }

    def "handles DataIntegrityViolationException"() {
        expect:
        mvc.perform(get("/fake/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath('$.errors[0].code').value("DATA_INTEGRITY_VIOLATION"))
    }

    def "handles generic Exception"() {
        expect:
        mvc.perform(get("/fake/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath('$.errors[0].code').value("INTERNAL_ERROR"))
    }
}
