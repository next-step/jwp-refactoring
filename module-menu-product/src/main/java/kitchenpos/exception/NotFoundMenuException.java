package kitchenpos.exception;

public class NotFoundMenuException extends RuntimeException {
    private static final String message = "존재하지 않는 메뉴입니다.";

    public static final NotFoundMenuException NOT_FOUND_MENU_EXCEPTION = new NotFoundMenuException(message);

    public NotFoundMenuException() {
        super(message);
    }

    public NotFoundMenuException(String message) {
        super(message);
    }
}
