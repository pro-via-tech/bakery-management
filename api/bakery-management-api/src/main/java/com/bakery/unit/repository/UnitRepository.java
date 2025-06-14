package com.bakery.unit.repository;

import com.bakery.unit.model.entity.Unit;
import com.bakery.unit.model.enums.UnitScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {


	Optional<Unit> findByNameIgnoreCase (String normalized);

	List<Unit> findByType (UnitScope type);
}
