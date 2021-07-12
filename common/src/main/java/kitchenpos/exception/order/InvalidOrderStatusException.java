package kitchenpos.exception.order;


public class InvalidOrderStatusException extends IllegalArgumentException {

    public InvalidOrderStatusException(String s) {
        super(s);
    }
}
