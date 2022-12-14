package kitchenpos.table.exception;

public class OrderTablesException extends RuntimeException{

    public OrderTablesException(OrderTablesExceptionType exceptionType) {
        super(exceptionType.message);
    }
}
