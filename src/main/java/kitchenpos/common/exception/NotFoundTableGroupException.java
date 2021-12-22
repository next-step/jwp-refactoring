package kitchenpos.common.exception;

public class NotFoundTableGroupException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "테이블 그룹을 찾을 수 없습니다.";

    public NotFoundTableGroupException() {
        super(EXCEPTION_MESSAGE);
    }
}
