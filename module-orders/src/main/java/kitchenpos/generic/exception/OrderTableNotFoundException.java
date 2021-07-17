package kitchenpos.generic.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public OrderTableNotFoundException(String message) {
        super(message);
    }
}
