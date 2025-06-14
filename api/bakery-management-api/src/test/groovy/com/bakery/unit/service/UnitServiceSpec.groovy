package com.bakery.unit.service

import com.bakery.common.exception.UnitNotFoundException
import com.bakery.common.exception.UnsupportedUnitException
import com.bakery.unit.mapper.UnitMapper
import com.bakery.unit.model.dto.UnitDTO
import com.bakery.unit.model.entity.Unit
import com.bakery.unit.model.enums.UnitScope
import com.bakery.unit.repository.UnitRepository
import spock.lang.Specification

class UnitServiceSpec extends Specification {

    def unitRepository = Mock(UnitRepository)
    def unitMapper = Mock(UnitMapper)
    def service = new UnitService(unitRepository, unitMapper)

    def "getAllUnits returns mapped unit DTOs"() {
        given:
        def unit1 = new Unit(name: "kg")
        def unit2 = new Unit(name: "g")
        def dto1 = new UnitDTO(name: "kg")
        def dto2 = new UnitDTO(name: "g")

        when:
        unitRepository.findAll() >> [unit1, unit2]
        unitMapper.toDto(unit1) >> dto1
        unitMapper.toDto(unit2) >> dto2

        def result = service.getAllUnits()

        then:
        result == [dto1, dto2]
    }

    def "getUnitById returns mapped unit when found"() {
        given:
        def unit = new Unit(id: 1L, name: "g")
        def dto = new UnitDTO(name: "g")

        when:
        unitRepository.findById(1L) >> Optional.of(unit)
        unitMapper.toDto(unit) >> dto

        def result = service.getUnitById(1L)

        then:
        result == dto
    }

    def "getUnitById throws when not found"() {
        when:
        unitRepository.findById(99L) >> Optional.empty()
        service.getUnitById(99L)

        then:
        thrown(UnitNotFoundException)
    }

    def "saveUnit maps and saves entity then returns DTO"() {
        given:
        def dto = new UnitDTO(name: "kg")
        def entity = new Unit(name: "kg")
        def saved = new Unit(id: 1L, name: "kg")
        def resultDto = new UnitDTO(id: 1L, name: "kg")

        when:
        unitMapper.toEntity(dto) >> entity
        unitRepository.save(entity) >> saved
        unitMapper.toDto(saved) >> resultDto

        def result = service.saveUnit(dto)

        then:
        result == resultDto
    }

    def "validateOrNormalize returns matched unit"() {
        given:
        def dto = new UnitDTO(name: "  KG  ")
        def unit = new Unit(name: "kg")

        when:
        unitRepository.findByNameIgnoreCase("kg") >> Optional.of(unit)

        def result = service.validateOrNormalize(dto)

        then:
        result == unit
    }

    def "validateOrNormalize throws if no match found"() {
        given:
        def dto = new UnitDTO(name: "NonExistent")

        when:
        unitRepository.findByNameIgnoreCase("nonexistent") >> Optional.empty()
        service.validateOrNormalize(dto)

        then:
        thrown(UnsupportedUnitException)
    }

    def "getUnitsByType returns filtered DTOs"() {
        given:
        def type = UnitScope.INGREDIENT
        def unit1 = new Unit(name: "g", type: type)
        def unit2 = new Unit(name: "kg", type: type)
        def dto1 = new UnitDTO(name: "g")
        def dto2 = new UnitDTO(name: "kg")

        when:
        unitRepository.findByType(type) >> [unit1, unit2]
        unitMapper.toDto(unit1) >> dto1
        unitMapper.toDto(unit2) >> dto2

        def result = service.getUnitsByType(type)

        then:
        result == [dto1, dto2]
    }
}

