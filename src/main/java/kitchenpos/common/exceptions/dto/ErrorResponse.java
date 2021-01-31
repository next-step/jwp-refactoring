package kitchenpos.common.exceptions.dto;

public class ErrorResponse {

	private String message;
	private String debugMessage;
	private String path;

	protected ErrorResponse() {
	}

	public ErrorResponse(final String message, final String debugMessage, final String path) {
		this.message = message;
		this.debugMessage = debugMessage;
		this.path = path;
	}

	public ErrorResponse(final String message, final String path) {
		this(message, null, path);
	}

	public String getMessage() {
		return message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public String getPath() {
		return path;
	}

}
