package kitchenpos.tablegroup.exception;

import kitchenpos.tablegroup.domain.domain.OrderTables;

public class InvalidOrderTablesException extends IllegalArgumentException {

	public static final String MESSAGE = String.format(
		"단체로 지정할 주문 테이블의 수는 %d와 같거나 커야합니다",
		OrderTables.MIN_SIZE_INCLUSIVE
	);

	public InvalidOrderTablesException() {
		super(MESSAGE);
	}
}
