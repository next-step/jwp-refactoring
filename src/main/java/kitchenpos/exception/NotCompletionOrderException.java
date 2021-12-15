package kitchenpos.exception;

public class NotCompletionOrderException extends RuntimeException {
    private static final String NOT_COMPLETION_ORDER = "Order 가 완료된 상태가 아닙니다.";

    public NotCompletionOrderException() {
        super(NOT_COMPLETION_ORDER);
    }
}
