package kitchenpos.order.exception;

public class NotFoundOrderLineItemsException extends IllegalArgumentException {

	public static final String MESSAGE = "주문항목 목록이 있어야 합니다.";

	public NotFoundOrderLineItemsException() {
		super(MESSAGE);
	}
}
