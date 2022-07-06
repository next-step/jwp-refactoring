package kitchenpos.exception;

public class UnCompletedOrderStatusException extends RuntimeException {
    public static final UnCompletedOrderStatusException UN_COMPLETED_ORDER_STATUS_EXCEPTION = new UnCompletedOrderStatusException(
            "주문상태가 완료가 아닙니다.");

    public UnCompletedOrderStatusException(String message) {
        super(message);
    }
}
