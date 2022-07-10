package kitchenpos.table.exception;

public class TableGroupInvalidStatusException extends RuntimeException {
    private static final String message = "이미 단체 지정이 되어있는 테이블은 단체 지정을 할 수 없습니다.";

    public TableGroupInvalidStatusException() {
        super(message);
    }
}
