package kitchenpos.tablegroup.exception;

public class IllegalGroupingTableStateException extends RuntimeException {

    private static final String ERROR_MESSAGE_ILLEGAL_TABLE_STATE = "그룹화 불가능한 주문테이블 입니다.";

    public IllegalGroupingTableStateException() {
        super(ERROR_MESSAGE_ILLEGAL_TABLE_STATE);
    }
}
