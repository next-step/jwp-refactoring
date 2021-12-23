package kitchenpos.menugroup.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundMenuGroupException extends NotFoundEntityException {

	public static final String MESSAGE = "메뉴 그룹을 찾을 수 없습니다.";

	public NotFoundMenuGroupException() {
		super(MESSAGE);
	}
}
