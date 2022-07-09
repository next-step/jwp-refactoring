package kitchenpos.order.exception;

public class EmptyOrderLineItemException extends RuntimeException {
    private static final String message = "주문시 주문항목은 1개 이상이어야 합니다.";

    public EmptyOrderLineItemException() {
        super(message);
    }
}
