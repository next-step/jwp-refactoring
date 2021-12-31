package kitchenpos.exception;

public class NotFoundOrderException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "주문을 찾을 수 없습니다.";

    public NotFoundOrderException() {
        super(EXCEPTION_MESSAGE);
    }
}
