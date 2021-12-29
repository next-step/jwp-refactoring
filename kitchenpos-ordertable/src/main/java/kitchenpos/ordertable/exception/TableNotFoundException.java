package kitchenpos.ordertable.exception;

public class TableNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_TABLE_NOT_EXIST = "존재하지 않는 주문 테이블입니다.";

    public TableNotFoundException() {
        super(ERROR_MESSAGE_TABLE_NOT_EXIST);
    }
}
