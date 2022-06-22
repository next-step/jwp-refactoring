package kitchenpos.exception;

import java.math.BigDecimal;

public class NegativePriceException extends RuntimeException {
    public static final String INVALID_PRICE = "Price는 0원 보다 작은 값일 수 없습니다. (input = %s)";

    public NegativePriceException(BigDecimal price) {
        super(String.format(INVALID_PRICE, price));
    }
}
