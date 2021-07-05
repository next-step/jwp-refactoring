package kitchenpos.table.exception;

public class NoOrderTableException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String NO_ORDER_TABLE = "상품 테이블이 없습니다. 유요한 입력값인지 확인하세요";

    public NoOrderTableException() {
        super(NO_ORDER_TABLE);
    }

    public NoOrderTableException(String message) {
        super(message);
    }
}
