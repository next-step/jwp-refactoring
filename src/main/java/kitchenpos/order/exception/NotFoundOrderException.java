package kitchenpos.order.exception;

public class NotFoundOrderException extends RuntimeException {
    public NotFoundOrderException() {
        super("존재하지 않은 주문 입니다.");
    }
}
