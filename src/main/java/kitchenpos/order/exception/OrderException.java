package kitchenpos.order.exception;

public class OrderException extends RuntimeException {

    public OrderException(OrderExceptionType exceptionType) {
        super(exceptionType.message);
    }
}
