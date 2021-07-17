package kitchenpos.common.exception;

public class CannotCreateOrderException extends RuntimeException {
    public CannotCreateOrderException() {
        super("주문을 생성할 수 없습니다.");
    }

    public CannotCreateOrderException(String message) {
        super(message);
    }
}
