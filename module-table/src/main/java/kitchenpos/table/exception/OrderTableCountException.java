package kitchenpos.table.exception;

public class OrderTableCountException extends IllegalArgumentException {

    public OrderTableCountException() {
        super("주문테이블은 2개 이상 등록할 수 있습니다.");
    }
}
