package kitchenpos.menu.exception;

public class NotFoundMenuGroupException extends RuntimeException {
    private static final String NOT_FOUND_MENU_GROUP_MESSAGE = "해당하는 메뉴그룹을 찾을 수 없습니다. (id = %s)";

    public NotFoundMenuGroupException(Long id) {
        super(String.format(NOT_FOUND_MENU_GROUP_MESSAGE, id));
    }
}
