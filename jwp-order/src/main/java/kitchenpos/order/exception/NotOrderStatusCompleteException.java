package kitchenpos.order.exception;

import kitchenpos.menu.exception.CustomException;

public class NotOrderStatusCompleteException extends CustomException {
    public static final String ORDER_STATUS_COMPLETED_MESSAGE = "완료된 상태로는 진행할 수 없습니다.";

    public NotOrderStatusCompleteException() {
        super(ORDER_STATUS_COMPLETED_MESSAGE);
    }
}
