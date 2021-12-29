package kitchenpos.common.exceptions;

public class NegativeNumberOfGuestsException extends CustomException {
    public static final String NEGATIVE_NUMBER_OF_GUESTS_MESSAGE = "손님 숫자는 0 미만이 될 수 없습니다.";

    public NegativeNumberOfGuestsException() {
        super(NEGATIVE_NUMBER_OF_GUESTS_MESSAGE);
    }
}
