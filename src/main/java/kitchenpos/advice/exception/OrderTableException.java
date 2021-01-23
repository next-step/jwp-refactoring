package kitchenpos.advice.exception;

public class OrderTableException extends RuntimeException {

    public OrderTableException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
