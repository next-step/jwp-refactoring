package kitchenpos.tablegroup.exception;

public class CanNotGroupByGroupingAlreadyException extends IllegalArgumentException {

	public static final String MESSAGE = "이미 단체 지정이 된 주문 테이블입니다.";

	public CanNotGroupByGroupingAlreadyException() {
		super(MESSAGE);
	}
}
