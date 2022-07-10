package kitchenpos.table.exception;

public class TableGroupNotFoundException extends RuntimeException {
    private static final String message = "단체 지정 정보를 찾을 수 없습니다.";

    public TableGroupNotFoundException() {
        super(message);
    }
}
