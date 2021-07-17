package common.valueobject.exception;

import common.error.BusinessException;

public class NegativePriceException extends BusinessException {
    public NegativePriceException(long minPrice) {
        super(String.format("가격은 필수값이며 %s보다 작을 수 없습니다.", minPrice));
    }

    public NegativePriceException(String message) {
        super(message);
    }
}
