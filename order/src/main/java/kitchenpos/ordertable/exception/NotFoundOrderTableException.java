package kitchenpos.ordertable.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundOrderTableException extends NotFoundEntityException {

	public static final String MESSAGE = "주문 테이블을 찾을 수 없습니다.";

	public NotFoundOrderTableException() {
		super(MESSAGE);
	}
}
