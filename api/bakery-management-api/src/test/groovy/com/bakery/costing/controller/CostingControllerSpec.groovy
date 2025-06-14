package com.bakery.costing.controller

import com.bakery.common.exception.GlobalExceptionHandler
import com.bakery.costing.dto.RecipeCostDTO
import com.bakery.costing.service.CostingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = CostingController)
@AutoConfigureMockMvc
class CostingControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    CostingService costingService = Mock(CostingService.class)

    def setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new CostingController(costingService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build()
    }

    def "GET /api/costing/recipe/id returns cost of recipe by ID"() {
        given:
        def dto = new RecipeCostDTO(recipeId: 1L, recipeName: "Cookies", costPerMainRecipe: BigDecimal.valueOf(14))
        costingService.calculateRecipeCost(1L) >> dto

        when:
        def result = mvc.perform(get("/api/costing/recipe/1"))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.data.recipeName', is('Cookies')))

    }
}
