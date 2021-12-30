package kitchenpos.order.exception;

public class NotCreatedMenuException extends RuntimeException {
    public NotCreatedMenuException() {
        super("상품이 생성되어 있지 않습니다.");
    }
}
