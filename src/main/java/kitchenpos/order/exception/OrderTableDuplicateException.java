package kitchenpos.order.exception;

public class OrderTableDuplicateException extends RuntimeException {
    private static final String message = "단체 지정시 주문 테이블은 중복될 수 없습니다.";

    public OrderTableDuplicateException() {
        super(message);
    }
}
