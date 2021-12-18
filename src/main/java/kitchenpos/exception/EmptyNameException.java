package kitchenpos.exception;

public class EmptyNameException extends RuntimeException {
    private static final String EMPTY_NAME_ERROR_MESSAGE = "이름이 비어있습니다. name=%s";

    public EmptyNameException(String name) {
        super(String.format(EMPTY_NAME_ERROR_MESSAGE, name));
    }
}
