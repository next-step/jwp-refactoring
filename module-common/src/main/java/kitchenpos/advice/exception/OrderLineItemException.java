package kitchenpos.advice.exception;

public class OrderLineItemException extends BusinessException {

    public OrderLineItemException(String message) {
        super(message);
    }

    public OrderLineItemException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
