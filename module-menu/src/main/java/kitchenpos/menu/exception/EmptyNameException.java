package kitchenpos.menu.exception;

public class EmptyNameException extends RuntimeException {
    public static final String CANT_EMPTY_IS_NAME = "이름은 빈값일 수 없습니다 (input = %s)";

    public EmptyNameException(String name) {
        super(String.format(CANT_EMPTY_IS_NAME, name));
    }
}
