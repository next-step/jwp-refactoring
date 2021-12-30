package kitchenpos.order.exception;

public class CompleteOrderChangeStateException extends RuntimeException {

    private static final String ERROR_MESSAGE_COMPLETE_ORDER_CANNOT_CHANGE = "계산 완료된 주문 상태는 변경할 수 없습니다.";

    public CompleteOrderChangeStateException() {
        super(ERROR_MESSAGE_COMPLETE_ORDER_CANNOT_CHANGE);
    }
}
