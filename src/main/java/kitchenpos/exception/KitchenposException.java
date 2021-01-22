package kitchenpos.exception;

public class KitchenposException extends RuntimeException {

    public KitchenposException() {
        super();
    }

    public KitchenposException(Object arg) {
        super(String.valueOf(arg));
    }
}
