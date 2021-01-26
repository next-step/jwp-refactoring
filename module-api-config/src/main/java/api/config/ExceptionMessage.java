package api.config;

public class ExceptionMessage {

	private String message;
	private String type;

	ExceptionMessage() {
	}

	public ExceptionMessage(Exception e) {
		this.message = e.getMessage();
		this.type = e.getClass().getSimpleName();
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}
}
