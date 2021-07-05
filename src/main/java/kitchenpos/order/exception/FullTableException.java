package kitchenpos.order.exception;

public class FullTableException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String FULL_TABLE = "사용중인 테이블입니다. 다른 테이블을 선책하세요";

    public FullTableException() {
        super(FULL_TABLE);
    }

    public FullTableException(String message) {
        super(message);
    }
}
