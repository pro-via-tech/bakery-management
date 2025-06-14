package com.bakery.unit.controller;

import com.bakery.common.api.ApiResponse;
import com.bakery.common.exception.IdNotAllowedException;
import com.bakery.unit.model.dto.UnitDTO;
import com.bakery.unit.model.enums.UnitScope;
import com.bakery.unit.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit")
@RequiredArgsConstructor
public class UnitController {

	private final UnitService unitService;

	@Operation(summary = "Get all units", description = "Retrieve all stored units")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UnitDTO>>> getAll() {
		return ResponseEntity.ok(ApiResponse.success(unitService.getAllUnits()));
	}

	@Operation(summary = "Add a new unit", description = "Register a new unit with name, display name, and conversion")
	@PostMapping
	public ResponseEntity<ApiResponse<UnitDTO>> create(@RequestBody @Valid UnitDTO dto) {
		if (dto.getId() != null) {
			throw new IdNotAllowedException();
		}
		return ResponseEntity.ok(ApiResponse.success(unitService.saveUnit(dto)));
	}

	@Operation(summary = "Retrieve unit type", description = "Retrieve all units by type in parameter")
	@GetMapping("/by-type")
	public ResponseEntity<ApiResponse<List<UnitDTO>>> getUnitsByType(@RequestParam("type") UnitScope type) {
		List<UnitDTO> units = unitService.getUnitsByType(type);
		return ResponseEntity.ok(ApiResponse.success(units));
	}
}
