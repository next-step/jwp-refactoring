package kitchenpos.order.exception;

public class NoOrderException extends RuntimeException {
    public NoOrderException() {
        super("해당 주문이 없습니다");
    }
}
