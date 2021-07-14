package kitchenpos.order.exception;

public class TableGroupNotFoundException extends RuntimeException {
    public TableGroupNotFoundException(String message) {
        super(message);
    }
}
