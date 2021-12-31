package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionStatusCode {

	WRONG_INPUT(ErrorCode.WRONG_INPUT, HttpStatus.BAD_REQUEST),
	DUPLICATE_INPUT(ErrorCode.DUPLICATE_INPUT, HttpStatus.BAD_REQUEST),
	NOT_FOUND(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND),
	INTERNAL_SERVER_ERROR(ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
	UNAUTHORIZED(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

	private ErrorCode errorCode;
	private HttpStatus httpStatus;

	ExceptionStatusCode(ErrorCode errorCode, HttpStatus httpStatus) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}

	public static HttpStatus getHttpStatusBy(ErrorCode errorCode) {
		return ExceptionStatusCode.valueOf(errorCode.name()).getHttpStatus();
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
