package kitchenpos.table.exception;

public class EmptyTableException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String EMPTY_TABLE = "빈 테이블의 손님 수를 수정할 수 없습니다/";

    public EmptyTableException() {
        super(EMPTY_TABLE);
    }

    public EmptyTableException(String message) {
        super(message);
    }
}
