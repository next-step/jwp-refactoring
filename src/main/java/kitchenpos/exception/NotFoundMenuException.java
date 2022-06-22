package kitchenpos.exception;

public class NotFoundMenuException extends RuntimeException {
    private static final String NOT_FOUND_MENU_MESSAGE = "해당하는 메뉴를 찾을 수 없습니다. (id = %s)";

    public NotFoundMenuException(Long id) {
        super(String.format(NOT_FOUND_MENU_MESSAGE, id));
    }
}
