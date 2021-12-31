package kitchenpos.order.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundOrderException extends NotFoundEntityException {

	public static final String MESSAGE = "주문을 찾을 수 없습니다.";

	public NotFoundOrderException() {
		super(MESSAGE);
	}
}

