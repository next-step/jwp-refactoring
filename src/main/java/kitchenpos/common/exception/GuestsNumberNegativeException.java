package kitchenpos.common.exception;

public class GuestsNumberNegativeException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "테이블 인원은 0명보다 작을 수 없습니다.";

    public GuestsNumberNegativeException() {
        super(EXCEPTION_MESSAGE);
    }
}
