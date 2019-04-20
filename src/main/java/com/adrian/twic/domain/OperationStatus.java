package com.adrian.twic.domain;

import com.adrian.twic.enums.OperationType;

public class OperationStatus {
	private final int code;
	private final String text;
	private final OperationType operationType;

	protected OperationStatus(final int code, final String text, final OperationType operationType) {
		this.code = code;
		this.text = text;
		this.operationType = operationType;
	}

	public static OperationStatus of(final int code, final String text, final OperationType operationType) {
		return new OperationStatus(code, text, operationType);
	}

	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	@Override
	public String toString() {
		return "OperationStatus [code=" + code + ", text=" + text + ", operationType=" + operationType + "]";
	}

}
