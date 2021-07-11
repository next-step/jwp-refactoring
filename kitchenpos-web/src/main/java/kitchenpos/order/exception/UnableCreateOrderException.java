package kitchenpos.order.exception;

public class UnableCreateOrderException extends IllegalArgumentException {
    public UnableCreateOrderException() {
    }

    public UnableCreateOrderException(String s) {
        super(s);
    }
}
