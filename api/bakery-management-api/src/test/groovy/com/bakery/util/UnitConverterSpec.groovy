package com.bakery.util

import com.bakery.ingredient.model.entity.Ingredient
import com.bakery.unit.model.entity.Unit
import spock.lang.Specification

import static java.math.BigDecimal.valueOf

class UnitConverterSpec extends Specification {

    def "toGrams returns correct conversion result"() {
        given:
        def unit = new Unit(conversionToGram: valueOf(100))
        def amount = valueOf(2)

        expect:
        UnitConverter.toGrams(amount, unit) == valueOf(200)
    }

    def "toGrams throws if unit is null"() {
        when:
        UnitConverter.toGrams(valueOf(1), null)

        then:
        thrown(IllegalArgumentException)
    }

    def "toGrams throws if conversion factor is null"() {
        given:
        def unit = new Unit(conversionToGram: null)

        when:
        UnitConverter.toGrams(valueOf(1), unit)

        then:
        thrown(IllegalArgumentException)
    }

    def "toGrams throws if conversion factor is zero or negative"() {
        when:
        UnitConverter.toGrams(valueOf(1), new Unit(conversionToGram: conversion))

        then:
        thrown(IllegalArgumentException)

        where:
        conversion << [valueOf(0), valueOf(-5)]
    }


    def "calculateCostPerGram returns expected value using unitWeightInGrams"() {
        given:
        def unit = new Unit(conversionToGram: valueOf(50))
        def ingredient = new Ingredient(
                purchaseCost: valueOf(20),
                purchaseQuantity: 10,
                unit: unit,
                unitWeightInGrams: valueOf(5)
        )

        expect:
        UnitConverter.calculateCostPerGram(ingredient) == valueOf(0.40)
    }

    def "calculateCostPerGram falls back to unit conversionToGram if unitWeightInGrams is null"() {
        given:
        def unit = new Unit(conversionToGram: valueOf(100))
        def ingredient = new Ingredient(
                purchaseCost: valueOf(10),
                purchaseQuantity: 5,
                unit: unit,
                unitWeightInGrams: null
        )

        expect:
        UnitConverter.calculateCostPerGram(ingredient) == valueOf(0.02)
    }

    def "calculateCostPerGram returns ZERO if gramsPerUnit is zero"() {
        given:
        def unit = new Unit(conversionToGram: valueOf(0))
        def ingredient = new Ingredient(
                purchaseCost: valueOf(10),
                purchaseQuantity: 5,
                unit: unit,
                unitWeightInGrams: null
        )

        expect:
        UnitConverter.calculateCostPerGram(ingredient) == BigDecimal.ZERO
    }

    def "calculateCostPerGram returns ZERO if ingredient or required fields are missing"() {
        expect:
        UnitConverter.calculateCostPerGram(input) == BigDecimal.ZERO

        where:
        input << [
                null,
                new Ingredient(purchaseCost: null, purchaseQuantity: 10, unit: new Unit(conversionToGram: valueOf(5))),
                new Ingredient(purchaseCost: valueOf(10), purchaseQuantity: null, unit: new Unit(conversionToGram: valueOf(5))),
                new Ingredient(purchaseCost: valueOf(10), purchaseQuantity: 0, unit: new Unit(conversionToGram: valueOf(5)))
        ]
    }

    def "calculateCostPerUnit returns correct cost per unit"() {
        given:
        def ingredient = new Ingredient(
                purchaseCost: valueOf(15),
                purchaseQuantity: 3
        )

        expect:
        UnitConverter.calculateCostPerUnit(ingredient) == valueOf(5.00)
    }
}