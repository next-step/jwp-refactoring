package kitchenpos.table.exception;

public class OrderTableEmptyException extends RuntimeException {
    public OrderTableEmptyException() {
        super("대상 테이블이 비어있습니다.");
    }

    public OrderTableEmptyException(String message) {
        super(message);
    }
}
