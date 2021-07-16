package kitchenpos.table.exception;

public class OrderTableIsNotEmptyException extends IllegalArgumentException {

    public OrderTableIsNotEmptyException() {
        super("주문테이블이 비어있지 않습니다.");
    }

}
