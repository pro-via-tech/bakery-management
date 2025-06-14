package com.bakery.common.exception;

public class UnitNotFoundException extends RuntimeException {
	public UnitNotFoundException(Long id) {super("Unit not found with id: " + id);
	}

}