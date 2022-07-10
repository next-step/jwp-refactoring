package kitchenpos.common.exception;

public class KitchenposException extends RuntimeException {
    private ErrorCode errorCode;

    public KitchenposException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
