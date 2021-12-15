package kitchenpos.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	PRICE_NOT_NEGATIVE_NUMBER(HttpStatus.BAD_REQUEST.value(), "가격은 0이상의 값만 가능합니다."),
	PRICE_IS_NOT_NULL(HttpStatus.BAD_REQUEST.value(), "가격을 입력해주세요.");

	private int errorCode;
	private String message;

	ErrorCode(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
}
