package kitchenpos.exception;

public class NotFoundMenuGroupException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "메뉴 그룹을 찾을 수 없습니다.";

    public NotFoundMenuGroupException() {
        super(EXCEPTION_MESSAGE);
    }
}
