package kitchenpos.order.exception;

public class NotFoundOrderException extends RuntimeException {

    public NotFoundOrderException() {
        super("주문을 찾을 수 없습니다.");
    }
}
