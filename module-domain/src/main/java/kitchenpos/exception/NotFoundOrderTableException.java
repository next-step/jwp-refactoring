package kitchenpos.exception;

public class NotFoundOrderTableException extends BadRequestException{
    private static final String EXCEPTION_MESSAGE = "주문 테이블을 찾을 수 없습니다.";

    public NotFoundOrderTableException() {
        super(EXCEPTION_MESSAGE);
    }
}
