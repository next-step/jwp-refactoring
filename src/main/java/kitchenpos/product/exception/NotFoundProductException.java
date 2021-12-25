package kitchenpos.product.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundProductException extends NotFoundEntityException {

	public static final String MESSAGE = "상품을 찾을 수 없습니다.";

	public NotFoundProductException() {
		super(MESSAGE);
	}
}
