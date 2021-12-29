package kitchenpos.ordertable.exception;

public class TableUpdateStateException extends RuntimeException {

    private static final String ERROR_MESSAGE_TABLE_IN_GROUP = "테이블 그룹에 속해있는 테이블은 상태를 변경할 수 없습니다.";

    public TableUpdateStateException() {
        super(ERROR_MESSAGE_TABLE_IN_GROUP);
    }
}
