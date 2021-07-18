package kitchenpos.common.exception;

public class CannotCreateOrderLineItemException extends RuntimeException {
    public CannotCreateOrderLineItemException() {
        super("주문 항목을 생성할 수 없습니다.");
    }

    public CannotCreateOrderLineItemException(String message) {
        super(message);
    }
}
