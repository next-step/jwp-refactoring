package kitchenpos.order.exception;

public class IllegalMenuIdsException extends IllegalArgumentException {
    public IllegalMenuIdsException(String message) {
        super(message);
    }
}
