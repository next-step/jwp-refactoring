package kitchenpos.table.exception;

public class OutOfOrderTableException extends RuntimeException {

    public OutOfOrderTableException() {
        super("단체 지정될 주문테이블은 2개 이상이어야 합니다.");
    }
}
