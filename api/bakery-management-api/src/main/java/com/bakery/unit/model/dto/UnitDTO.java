package com.bakery.unit.model.dto;

import com.bakery.unit.model.enums.UnitScope;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitDTO {
	private Long id;
	private String name;
	private String displayName;
	private BigDecimal conversionToGram;
	private UnitScope type;
}
