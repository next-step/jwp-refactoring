package kitchenpos.exception;

public class OrderTableAlreadyEmptyException extends RuntimeException {
    public static final OrderTableAlreadyEmptyException ORDER_TABLE_ALREADY_EMPTY_EXCEPTION = new OrderTableAlreadyEmptyException(
            "주문 테이블이 이미 빈 테이블 입니다.");

    public OrderTableAlreadyEmptyException(String message) {
        super(message);
    }
}
