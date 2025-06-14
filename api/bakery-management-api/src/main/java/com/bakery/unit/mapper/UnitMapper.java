package com.bakery.unit.mapper;


import com.bakery.unit.model.dto.UnitDTO;
import com.bakery.unit.model.entity.Unit;
import com.bakery.unit.model.enums.UnitScope;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface UnitMapper {

	@Mappings({
		@Mapping(target = "conversionToGram", expression = "java(mapConversion(unit))")
	})
	UnitDTO toDto(Unit unit);

	Unit toEntity(UnitDTO dto);

	default BigDecimal mapConversion(Unit unit) {
		return unit.getType() == UnitScope.RECIPE ? null : unit.getConversionToGram();
	}
}
