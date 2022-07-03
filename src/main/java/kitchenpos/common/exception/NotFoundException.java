package kitchenpos.common.exception;

public class NotFoundException extends KitchenposException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
