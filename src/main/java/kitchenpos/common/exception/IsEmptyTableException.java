package kitchenpos.common.exception;

public class IsEmptyTableException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "빈 테이블의 인원은 변경 할 수 없습니다.";

    public IsEmptyTableException() {
        super(EXCEPTION_MESSAGE);
    }
}
