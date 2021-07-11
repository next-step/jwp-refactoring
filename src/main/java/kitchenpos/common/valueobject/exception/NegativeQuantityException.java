package kitchenpos.common.valueobject.exception;

import kitchenpos.common.error.exception.BusinessException;

public class NegativeQuantityException extends BusinessException {
    public NegativeQuantityException(long quantity) {
        super(String.format("갯수는 %s보다 작을 수 없습니다.", quantity));
    }

    public NegativeQuantityException(String message) {
        super(message);
    }
}
