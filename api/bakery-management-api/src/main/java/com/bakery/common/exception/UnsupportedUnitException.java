package com.bakery.common.exception;

public class UnsupportedUnitException extends RuntimeException {
	public UnsupportedUnitException (String unitName) {
		super("Unsupported unit: " + unitName);
	}
}
