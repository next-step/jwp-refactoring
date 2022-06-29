package kitchenpos.table.exception;

public class OrderTableAlreadyEmptyException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "해당 주문 테이블은 이미 빈 테이블입니다. (id = %s)";

    public OrderTableAlreadyEmptyException(Long id) {
        super(String.format(EXCEPTION_MESSAGE, id));
    }
}
