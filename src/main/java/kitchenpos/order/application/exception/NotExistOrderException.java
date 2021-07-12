package kitchenpos.order.application.exception;

import kitchenpos.common.error.exception.NotExistException;

public class NotExistOrderException extends NotExistException {
    public NotExistOrderException() {
        super("해당하는 주문을 찾을 수 없습니다.");
    }

    public NotExistOrderException(String message) {
        super(message);
    }
}
