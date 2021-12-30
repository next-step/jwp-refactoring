package kitchenpos.common.exceptions;

public class NotOrderStatusException extends CustomException {
    public static final String NOT_CHAGNE_ORDER_STATUS_MESSAGE = "요청한 상태는 변경 불가능합니다.";

    public NotOrderStatusException() {
        super(NOT_CHAGNE_ORDER_STATUS_MESSAGE);
    }
}
