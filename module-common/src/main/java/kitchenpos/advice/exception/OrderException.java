package kitchenpos.advice.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }

    public OrderException(String message, String orderStatus) {
        super(String.format(message + " %s ", orderStatus));
    }
}
