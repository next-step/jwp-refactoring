package kitchenpos.common.exception;

public class NotFoundMenuException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "메뉴를 찾을 수 없습니다.";

    public NotFoundMenuException() {
        super(EXCEPTION_MESSAGE);
    }
}
