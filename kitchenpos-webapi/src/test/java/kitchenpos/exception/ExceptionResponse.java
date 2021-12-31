package kitchenpos.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

	private String message;
	private String description;
	private LocalDateTime createdDate;

	public ExceptionResponse(HttpStatus httpStatus, String message, String description, LocalDateTime createdDate) {
		this.message = message;
		this.description = description;
		this.createdDate = createdDate;
	}

	public ExceptionResponse(AppException appException) {
		this.message = appException.getErrorCode().getMessage();
		this.description = appException.getDescription();
		this.createdDate = LocalDateTime.now();
	}

	public String getMessage() {
		return message;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}
