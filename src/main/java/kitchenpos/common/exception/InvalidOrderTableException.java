package kitchenpos.common.exception;

public class InvalidOrderTableException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "주문테이블이 비어있지 않거나 이미 테이블 그룹에 배정되었습니다.";

    public InvalidOrderTableException() {
        super(EXCEPTION_MESSAGE);
    }
}
