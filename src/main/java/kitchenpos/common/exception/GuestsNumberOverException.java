package kitchenpos.common.exception;

public class GuestsNumberOverException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "테이블 인원은 0명보다 작을 수 없습니다.";

    public GuestsNumberOverException() {
        super(EXCEPTION_MESSAGE);
    }
}
