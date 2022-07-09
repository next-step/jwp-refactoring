package kitchenpos.table.exception;

public class TableGroupNotEmptyException extends RuntimeException {
    private static final String message = "비어있지 않은 테이블은 단체 지정을 할 수 없습니다.";

    public TableGroupNotEmptyException() {
        super(message);
    }
}
