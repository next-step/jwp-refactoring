package kitchenpos.order.exception;

public class CompletedOrderException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String COMPLETED_ORDER = "이미 정산된 주문입니다.";

    public CompletedOrderException() {
        super(COMPLETED_ORDER);
    }

    public CompletedOrderException(String message) {
        super(message);
    }
}
