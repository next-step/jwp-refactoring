package common.valueobject.exception;

import common.error.exception.BusinessException;

public class NegativeQuantityException extends BusinessException {
    public NegativeQuantityException(long minQuantity) {
        super(String.format("갯수는 %s보다 작을 수 없습니다.", minQuantity));
    }

    public NegativeQuantityException(String message) {
        super(message);
    }
}
