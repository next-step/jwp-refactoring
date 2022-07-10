package kitchenpos.order.exception;

public class OrderAlreadyCompletedException extends RuntimeException {
    private static final String message = "계산 완료된 주문은 상태를 변경할 수 없습니다.";

    public OrderAlreadyCompletedException() {
        super(message);
    }
}
