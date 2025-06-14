package com.bakery.costing.controller;

import com.bakery.common.api.ApiResponse;
import com.bakery.costing.dto.RecipeCostDTO;
import com.bakery.costing.service.CostingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/costing")
@RequiredArgsConstructor
public class CostingController {

	private final CostingService costingService;

	@Operation(summary = "Get recipe cost", description = "Get the cost of a recipe by recipe id")
	@GetMapping("/recipe/{id}")
	public ResponseEntity<ApiResponse<RecipeCostDTO>> getRecipeCost(@PathVariable Long id) {
		RecipeCostDTO costDTO = costingService.calculateRecipeCost(id);
		return ResponseEntity.ok(ApiResponse.success(costDTO));
	}

	@Operation(summary = "Placeholder", description = "Placeholder")
	@GetMapping("/placeholder")
	public ResponseEntity<ApiResponse<Object>> placeholder() {
		return ResponseEntity.ok(ApiResponse.success("This endpoint does nothing"));
	}
}
