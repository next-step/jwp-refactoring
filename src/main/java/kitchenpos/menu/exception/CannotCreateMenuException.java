package kitchenpos.menu.exception;

public class CannotCreateMenuException extends RuntimeException {
    public CannotCreateMenuException() {
        super("메뉴를 생성할 수 없습니다.");
    }

    public CannotCreateMenuException(String message) {
        super(message);
    }
}
