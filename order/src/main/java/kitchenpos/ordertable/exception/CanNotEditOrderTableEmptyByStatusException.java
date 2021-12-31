package kitchenpos.ordertable.exception;

public class CanNotEditOrderTableEmptyByStatusException extends IllegalArgumentException {

	public static final String MESSAGE = "주문 테이블이 '조리' 혹은 식사' 상태면 수정할 수 없습니다.";

	public CanNotEditOrderTableEmptyByStatusException() {
		super(MESSAGE);
	}
}
