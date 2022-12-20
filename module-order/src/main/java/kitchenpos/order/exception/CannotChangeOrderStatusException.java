package kitchenpos.order.exception;

public class CannotChangeOrderStatusException extends RuntimeException {

    public CannotChangeOrderStatusException() {
        super("주문 상태를 변경할 수 없습니다.");
    }
}
