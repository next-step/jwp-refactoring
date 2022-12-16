package kitchenpos.table.exception;

public class OrderTableException extends RuntimeException {

    public OrderTableException(OrderTableExceptionType exceptionType) {
        super(exceptionType.message);
    }

    public OrderTableException(String message) {
        super(message);
    }
}
