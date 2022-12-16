package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class CustomErrorResponse {

	private String statusName;

	private int statusCode;
	private String errorMessage;

	protected CustomErrorResponse() {}

	private CustomErrorResponse(String errorMessage, HttpStatus httpStatus) {
		this.errorMessage = errorMessage;
		this.statusName = httpStatus.name();
		this.statusCode = httpStatus.value();
	}

	public static CustomErrorResponse of(String errorMessage, HttpStatus httpStatus) {
		return new CustomErrorResponse(errorMessage, httpStatus);
	}

	public String getStatusName() {
		return statusName;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
