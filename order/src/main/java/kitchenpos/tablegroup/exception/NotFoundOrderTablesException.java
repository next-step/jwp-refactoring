package kitchenpos.tablegroup.exception;

public class NotFoundOrderTablesException extends IllegalArgumentException {

	public static final String MESSAGE = "단체로 지정할 주문 테이블 목록이 있어야 합니다.";

	public NotFoundOrderTablesException() {
		super(MESSAGE);
	}
}
