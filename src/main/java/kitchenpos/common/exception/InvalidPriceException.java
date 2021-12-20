package kitchenpos.common.exception;

public class InvalidPriceException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "가격은 0원 이상이어야 합니다.";

    public InvalidPriceException() {
        super(EXCEPTION_MESSAGE);
    }
}
