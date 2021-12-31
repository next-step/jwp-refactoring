package kitchenpos.menu.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundMenuProductException extends NotFoundEntityException {

	public static final String MESSAGE = "메뉴 상품을 찾을 수 없습니다.";

	public NotFoundMenuProductException() {
		super(MESSAGE);
	}
}
