package common.exception;

public class EmptyNameException extends CustomException {
    private static final String EMPTY_NAME_MESSAGE = "이름은 필수로 입력되어야 합니다.";

    public EmptyNameException() {
        super(EMPTY_NAME_MESSAGE);
    }
}
