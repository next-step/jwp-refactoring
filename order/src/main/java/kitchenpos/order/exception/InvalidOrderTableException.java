package kitchenpos.order.exception;

public class InvalidOrderTableException extends IllegalArgumentException {

	public static final String MESSAGE = "주문한 테이블은 비어있을 수 없습니다.";

	public InvalidOrderTableException() {
		super(MESSAGE);
	}
}
