package kitchenpos.exception;

public class CustomException extends RuntimeException {
	private String errorMessage;

	public CustomException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
