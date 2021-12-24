package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class PriceGreaterThenSumException extends CustomException {
    private static final String NO_PRICE_GREATER_THEN_SUM = "메뉴의 가격은 상품들의 합한 가격보다 많을 수 없습니다.";

    public PriceGreaterThenSumException(final HttpStatus httpStatus) {
        super(httpStatus, NO_PRICE_GREATER_THEN_SUM);
    }
}
