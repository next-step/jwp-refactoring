package kitchenpos.tablegroup.exception;

public class NoTableSizeException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_TABLE_SIZE = "2개 이상의 테이블을 선택하세요";

    public NoTableSizeException() {
        super(NO_TABLE_SIZE);
    }

    public NoTableSizeException(String message) {
        super(message);
    }
}
