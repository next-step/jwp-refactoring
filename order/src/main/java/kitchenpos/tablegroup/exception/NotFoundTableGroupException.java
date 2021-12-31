package kitchenpos.tablegroup.exception;

import kitchenpos.common.NotFoundEntityException;

public class NotFoundTableGroupException extends NotFoundEntityException {

	public static final String MESSAGE = "테이블 그룹을 찾을 수 없습니다.";

	public NotFoundTableGroupException() {
		super(MESSAGE);
	}
}
