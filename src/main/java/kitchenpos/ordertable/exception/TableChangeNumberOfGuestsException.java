package kitchenpos.ordertable.exception;

public class TableChangeNumberOfGuestsException extends RuntimeException {

    private static final String ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED = "주문 종료 상태에선 방문 손님 수를 변경할 수 없습니다.";

    public TableChangeNumberOfGuestsException() {
        super(ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED);
    }
}
