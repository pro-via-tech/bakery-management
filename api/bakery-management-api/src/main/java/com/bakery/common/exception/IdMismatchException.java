package com.bakery.common.exception;

public class IdMismatchException extends RuntimeException {
	public IdMismatchException(Long pathId, Long bodyId) {
		super(String.format("ID in path (%d) does not match ID in body (%d).", pathId, bodyId));
	}
}
