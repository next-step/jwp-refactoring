package kitchenpos.common.exception;

public class BadRequestException extends KitchenposException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
