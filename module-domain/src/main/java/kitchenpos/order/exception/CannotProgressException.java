package kitchenpos.order.exception;

public class CannotProgressException extends RuntimeException {
    public CannotProgressException() {
        super("주문 상태를 변경할 수 없습니다");
    }
}
