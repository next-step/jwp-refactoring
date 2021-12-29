package kitchenpos.common.exceptions;

public class NoRequiredInputPriceException extends CustomException {
    private static final String NO_REQUIRED_INPUT_PRICE = "가격이 입력되지 않았습니다.";

    public NoRequiredInputPriceException() {
        super(NO_REQUIRED_INPUT_PRICE);
    }
}
