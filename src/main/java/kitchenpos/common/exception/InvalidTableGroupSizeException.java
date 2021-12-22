package kitchenpos.common.exception;

public class InvalidTableGroupSizeException extends BadRequestException{
    private static final String EXCEPTION_MESSAGE = "주문 테이블은 2개 이상이여야 합니다.";

    public InvalidTableGroupSizeException() {
        super(EXCEPTION_MESSAGE);
    }
}
