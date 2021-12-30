package kitchenpos.order.exception;

public class NotFoundOrderLineItemException extends RuntimeException {

    private static final String ERROR_MESSAGE_NO_ITEMS = "주문 항목이 없습니다.";

    public NotFoundOrderLineItemException() {
        super(ERROR_MESSAGE_NO_ITEMS);
    }
}
