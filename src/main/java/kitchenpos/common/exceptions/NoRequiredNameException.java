package kitchenpos.common.exceptions;

public class NoRequiredNameException extends CustomException {
    private static final String NO_REQUIRED_INPUT_NAME = "이름이 입력되지 않았습니다.";

    public NoRequiredNameException() {
        super(NO_REQUIRED_INPUT_NAME);
    }
}
