package kitchenpos.table.exception;

public class OrderTablesException extends RuntimeException {

    public OrderTablesException(OrderTablesExceptionType exceptionType) {
        super(exceptionType.message);
    }

    public OrderTablesException(String message) {
        super(message);
    }
}
