package kitchenpos.common.exceptions;

public class OrderStatusCompletedException extends CustomException {
    public static final String ORDER_STATUS_COMPLETED_MESSAGE = "완료된 상태로는 진행할 수 없습니다.";

    public OrderStatusCompletedException() {
        super(ORDER_STATUS_COMPLETED_MESSAGE);
    }
}
