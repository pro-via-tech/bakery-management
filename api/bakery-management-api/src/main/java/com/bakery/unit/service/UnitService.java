package com.bakery.unit.service;

import com.bakery.common.exception.UnitNotFoundException;
import com.bakery.common.exception.UnsupportedUnitException;
import com.bakery.unit.mapper.UnitMapper;
import com.bakery.unit.model.dto.UnitDTO;
import com.bakery.unit.model.entity.Unit;
import com.bakery.unit.model.enums.UnitScope;
import com.bakery.unit.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

	private final UnitRepository unitRepository;
	private final UnitMapper unitMapper;

	public List<UnitDTO> getAllUnits () {
		return unitRepository.findAll()
			.stream()
			.map(unitMapper::toDto)
			.toList();
	}

	public UnitDTO getUnitById(Long id) {
		Unit unit = unitRepository.findById(id)
			.orElseThrow(() -> new UnitNotFoundException(id));
		return unitMapper.toDto(unit);
	}

	public UnitDTO saveUnit(UnitDTO	dto) {
		Unit unit = unitMapper.toEntity(dto);
		return unitMapper.toDto(unitRepository.save(unit));
	}

	public Unit validateOrNormalize(UnitDTO dto) {
		String normalized = dto.getName().trim().toLowerCase();

		return unitRepository.findByNameIgnoreCase(normalized)
			.orElseThrow(() -> new UnsupportedUnitException(dto.getName()));
	}

	public List<UnitDTO> getUnitsByType (UnitScope type) {
		return unitRepository.findByType(type).stream()
			.map(unitMapper::toDto)
			.toList();
	}
}

