package kitchenpos.common;

public abstract class NotFoundEntityException extends RuntimeException {
	protected NotFoundEntityException(String message) {
		super(message);
	}
}
