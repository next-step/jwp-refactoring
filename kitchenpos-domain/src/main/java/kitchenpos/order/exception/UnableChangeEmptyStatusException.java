package kitchenpos.order.exception;

public class UnableChangeEmptyStatusException extends IllegalArgumentException {
    public UnableChangeEmptyStatusException() {
    }

    public UnableChangeEmptyStatusException(String s) {
        super(s);
    }
}
