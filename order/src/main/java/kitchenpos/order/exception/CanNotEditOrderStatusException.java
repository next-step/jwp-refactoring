package kitchenpos.order.exception;

public class CanNotEditOrderStatusException extends IllegalArgumentException {

	public static final String MESSAGE = "계산완료된 주문의 상태 변경은 불가합니다.";

	public CanNotEditOrderStatusException() {
		super(MESSAGE);
	}
}

