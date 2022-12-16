package kitchenpos.exception;

public class EntityNotFoundException extends CustomException {
	public EntityNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
