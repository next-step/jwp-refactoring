package kitchenpos.tablegroup.exception;

public class NotAbaliableOrderTableException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String NO_TABLE = "유효하지 않는 테이블입니다. 입렵값을 확인하세요";

    public NotAbaliableOrderTableException() {
        super(NO_TABLE);
    }

    public NotAbaliableOrderTableException(String message) {
        super(message);
    }
}
