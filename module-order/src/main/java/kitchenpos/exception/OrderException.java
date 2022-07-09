package kitchenpos.exception;

public class OrderException extends RuntimeException {
    public OrderException(final OrderExceptionType orderExceptionType) {
        super(orderExceptionType.getMessage());
    }

    public OrderException(final String message) {
        super(message);
    }
}
