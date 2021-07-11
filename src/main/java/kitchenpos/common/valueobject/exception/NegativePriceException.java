package kitchenpos.common.valueobject.exception;

import kitchenpos.common.error.exception.BusinessException;

public class NegativePriceException extends BusinessException {
    public NegativePriceException(long price) {
        super(String.format("가격은 필수값이며 %s보다 작을 수 없습니다.", price));
    }

    public NegativePriceException(String message) {
        super(message);
    }
}
