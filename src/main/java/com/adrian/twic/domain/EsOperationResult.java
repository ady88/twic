package com.adrian.twic.domain;

import com.adrian.twic.enums.OperationType;

/**
 * Holds data about the result of a elasticsearch operation.
 */
public final class EsOperationResult extends OperationStatus {

	protected EsOperationResult(int code, String text, OperationType operationType) {
		super(code, text, operationType);
	}

	public static EsOperationResult of(final int code, final String text, final OperationType operationType) {
		return new EsOperationResult(code, text, operationType);
	}
}
