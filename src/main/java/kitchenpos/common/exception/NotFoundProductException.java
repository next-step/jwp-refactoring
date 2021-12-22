package kitchenpos.common.exception;

public class NotFoundProductException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "상품을 찾을 수 없습니다.";

    public NotFoundProductException() {
        super(EXCEPTION_MESSAGE);
    }
}
