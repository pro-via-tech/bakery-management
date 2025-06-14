package com.bakery.common.exception;

public class IdNotAllowedException extends RuntimeException {
	public IdNotAllowedException() {
		super("ID must not be provided for this operation.");
	}
}
