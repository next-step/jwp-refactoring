package kitchenpos.tablegroup.exception;

public class CanNotGroupByEmptyException extends IllegalArgumentException {

	public static final String MESSAGE = "단체로 지정할 주문 테이블은 비어있어야 합니다.";

	public CanNotGroupByEmptyException() {
		super(MESSAGE);
	}
}
