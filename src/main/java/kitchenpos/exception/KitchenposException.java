package kitchenpos.exception;

public class KitchenposException extends RuntimeException {
    public KitchenposException(KitchenposErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
