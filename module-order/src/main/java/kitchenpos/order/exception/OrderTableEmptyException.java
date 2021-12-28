package kitchenpos.order.exception;

public class OrderTableEmptyException extends IllegalArgumentException {

    private static final String MESSAGE = "주문 테이블이 비어있습니다.";

    public OrderTableEmptyException() {
        super(MESSAGE);
    }
}
