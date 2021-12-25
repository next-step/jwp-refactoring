package kitchenpos.menu.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundMenuException extends NotFoundEntityException {

	public static final String MESSAGE = "메뉴를 찾을 수 없습니다.";

	public NotFoundMenuException() {
		super(MESSAGE);
	}
}
