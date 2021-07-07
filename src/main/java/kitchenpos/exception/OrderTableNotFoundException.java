package kitchenpos.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public OrderTableNotFoundException(String message) {
        super(message);
    }
}
