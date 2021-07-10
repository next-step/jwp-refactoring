package kitchenpos.exception;

public class NotAvaliableTableException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NOT_AVALIVBLE_TABLE = "현재 사용중이지 않은 테이블 입니다. 테이블을 확인하세요";

    public NotAvaliableTableException() {
        super(NOT_AVALIVBLE_TABLE);
    }

    public NotAvaliableTableException(String message) {
        super(message);
    }
}
