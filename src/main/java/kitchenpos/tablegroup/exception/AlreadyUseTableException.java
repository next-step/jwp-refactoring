package kitchenpos.tablegroup.exception;

public class AlreadyUseTableException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String ALREADY_USE_TABLE = "이미 사용중인 테이블입니다.";

    public AlreadyUseTableException() {
        super(ALREADY_USE_TABLE);
    }

    public AlreadyUseTableException(String message) {
        super(message);
    }
}
