package kitchenpos.common.exceptions;

public class EmptyOrderStatusException extends CustomException {
    public static final String EMPTY_ORDER_TABLE_STATUS_MESSAGE = "주문 상태가 없습니다.";

    public EmptyOrderStatusException() {
        super(EMPTY_ORDER_TABLE_STATUS_MESSAGE);
    }
}
