package kitchenpos.common.exception;

public class OrderStatusNotCompletedException extends RuntimeException {
    public static final String ORDER_STATUS_NOT_COMPLETED_MESSAGE = "완료된 주문 상태가 아닙니다.";

    public OrderStatusNotCompletedException() {
        super(ORDER_STATUS_NOT_COMPLETED_MESSAGE);
    }
}
