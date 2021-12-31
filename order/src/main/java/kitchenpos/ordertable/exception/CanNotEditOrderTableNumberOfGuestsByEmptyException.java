package kitchenpos.ordertable.exception;

public class CanNotEditOrderTableNumberOfGuestsByEmptyException extends IllegalArgumentException {

	public static final String MESSAGE = "주문 테이블이 비어있으면 손님 수를 수정할 수 없습니다.";

	public CanNotEditOrderTableNumberOfGuestsByEmptyException() {
		super(MESSAGE);
	}
}
