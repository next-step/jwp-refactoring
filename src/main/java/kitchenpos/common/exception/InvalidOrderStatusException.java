package kitchenpos.common.exception;

public class InvalidOrderStatusException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "주문 상태가 유효하지 않습니다.";

    public InvalidOrderStatusException() {
        super(EXCEPTION_MESSAGE);
    }
}
