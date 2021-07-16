package kitchenpos.table.exception;

public class OrderTableIsEmptyException extends IllegalArgumentException {

    public OrderTableIsEmptyException() {
        super("주문테이블이 비어있습니다.");
    }
}
