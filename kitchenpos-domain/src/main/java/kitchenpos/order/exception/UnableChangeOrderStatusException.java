package kitchenpos.order.exception;

public class UnableChangeOrderStatusException extends IllegalArgumentException {
    public UnableChangeOrderStatusException() {
    }

    public UnableChangeOrderStatusException(String s) {
        super(s);
    }
}
