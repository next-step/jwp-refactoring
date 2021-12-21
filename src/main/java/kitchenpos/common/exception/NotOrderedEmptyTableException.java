package kitchenpos.common.exception;

public class NotOrderedEmptyTableException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "빈 테이블에서는 주문을 할 수 없습니다.";

    public NotOrderedEmptyTableException() {
        super(EXCEPTION_MESSAGE);
    }
}
