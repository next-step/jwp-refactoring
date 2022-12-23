package kitchenpos.common.exception;

public class KitchenposException extends RuntimeException {
    private final ErrorCode errorCode;

    public KitchenposException(ErrorCode errorCode){
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
