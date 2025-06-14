package com.bakery.ingredient.service

import com.bakery.common.exception.IngredientNotFoundException
import com.bakery.common.exception.InvalidUnitConfigurationException
import com.bakery.ingredient.mapper.IngredientMapper
import com.bakery.ingredient.model.dto.IngredientDTO
import com.bakery.ingredient.model.entity.Ingredient
import com.bakery.ingredient.repository.IngredientRepository
import com.bakery.unit.mapper.UnitMapper
import com.bakery.unit.model.dto.UnitDTO
import com.bakery.unit.model.entity.Unit
import com.bakery.unit.model.enums.UnitScope
import com.bakery.unit.service.UnitService
import spock.lang.Specification

class IngredientServiceSpec extends Specification {

    def repo = Mock(IngredientRepository)
    def mapper = Mock(IngredientMapper)
    def unitService = Mock(UnitService)
    def unitMapper = Mock(UnitMapper)
    def service = new IngredientService(repo, mapper, unitService, unitMapper)

    def "getAllIngredients returns mapped ingredients"() {
        given:
        def ingredients = [Mock(Ingredient)]
        def dtos = [Mock(IngredientDTO)]
        repo.findAllWithUnit() >> ingredients
        mapper.toDto(_) >> dtos[0]

        expect:
        service.getAllIngredients() == dtos
    }

    def "getIngredientById returns mapped DTO when found"() {
        given:
        def ingredient = Mock(Ingredient)
        def dto = Mock(IngredientDTO)
        repo.findByIdWithUnit(1L) >> Optional.of(ingredient)
        mapper.toDto(ingredient) >> dto

        expect:
        service.getIngredientById(1L) == dto
    }

    def "getIngredientById throws when not found"() {
        given:
        repo.findByIdWithUnit(999L) >> Optional.empty()

        when:
        service.getIngredientById(999L)

        then:
        thrown(IngredientNotFoundException)
    }

    def "saveIngredient validates, calculates, and returns DTO"() {
        given:
        def dto = new IngredientDTO(name: "Flour", unit: new UnitDTO(name: "g", type: UnitScope.INGREDIENT))
        def unit = new Unit(name: "g", type: UnitScope.INGREDIENT, conversionToGram: 1)
        def entity = new Ingredient(name: "Flour", unit: unit, purchaseCost: 10, purchaseQuantity: 3)
        def saved = new Ingredient(name: "Flour", unit: unit)
        def outDto = new IngredientDTO(name: "Flour")

        unitService.validateOrNormalize(dto.unit) >> unit
        unitMapper.toDto(unit) >> dto.unit
        mapper.toEntity(dto) >> entity
        repo.save(entity) >> saved
        mapper.toDto(saved) >> outDto

        expect:
        service.saveIngredient(dto) == outDto
    }

    def "updateIngredient updates and returns DTO"() {
        given:
        def dto = new IngredientDTO(name: "Sugar", unit: new UnitDTO(name: "kg", type: UnitScope.INGREDIENT))
        def unit = new Unit(name: "kg", type: UnitScope.INGREDIENT, conversionToGram: 1000)
        def existing = new Ingredient(id: 1L, name: "Sugar", unit: unit)
        def updated = new Ingredient(name: "Sugar", unit: unit)
        def outDto = new IngredientDTO(name: "Sugar")

        repo.findByIdWithUnit(1L) >> Optional.of(existing)
        unitService.validateOrNormalize(dto.unit) >> unit
        unitMapper.toDto(unit) >> dto.unit
        mapper.updateEntityFromDto(dto, existing) >> _
        repo.save(existing) >> updated
        mapper.toDto(updated) >> outDto

        expect:
        service.updateIngredient(1L, dto) == outDto
    }

    def "updateIngredient throws if not found"() {
        given:
        repo.findByIdWithUnit(999L) >> Optional.empty()

        when:
        service.updateIngredient(999L, new IngredientDTO())

        then:
        thrown(IngredientNotFoundException)
    }

    def "deleteIngredient deletes if exists"() {
        given:
        repo.existsById(1L) >> true

        when:
        service.deleteIngredient(1L)

        then:
        1 * repo.deleteById(1L)
    }

    def "deleteIngredient throws if not exists"() {
        given:
        repo.existsById(99L) >> false

        when:
        service.deleteIngredient(99L)

        then:
        thrown(IngredientNotFoundException)
    }

    def "validateUnitConfiguration throws on bad discrete unit"() {
        given:
        def unit = new Unit(name: "egg", type: UnitScope.INGREDIENT, discrete: true)
        def ingredient = new Ingredient(name: "Egg", unit: unit, unitWeightInGrams: BigDecimal.ZERO)

        when:
        service.saveIngredient(new IngredientDTO(name: "Egg", unit: new UnitDTO(name: "egg", type: UnitScope.INGREDIENT)))

        then:
        unitService.validateOrNormalize(_) >> unit
        unitMapper.toDto(_) >> new UnitDTO(name: "egg", type: UnitScope.INGREDIENT)
        mapper.toEntity(_) >> ingredient
        thrown(InvalidUnitConfigurationException)
    }
}
