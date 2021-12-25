package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoRequiredInputPriceException extends CustomException {
    private static final String NO_REQUIRED_INPUT_PRICE = "가격이 입력되지 않았습니다.";

    public NoRequiredInputPriceException(final HttpStatus httpStatus) {
        super(httpStatus, NO_REQUIRED_INPUT_PRICE);
    }
}
