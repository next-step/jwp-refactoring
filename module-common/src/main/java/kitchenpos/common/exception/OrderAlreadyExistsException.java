package kitchenpos.common.exception;

public class OrderAlreadyExistsException extends RuntimeException {
    public OrderAlreadyExistsException() {
        super("주문이 이미 존재합니다.");
    }

    public OrderAlreadyExistsException(String message) {
        super(message);
    }
}
