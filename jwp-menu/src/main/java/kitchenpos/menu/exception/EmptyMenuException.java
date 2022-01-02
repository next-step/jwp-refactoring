package kitchenpos.menu.exception;

public class EmptyMenuException extends CustomException {
    public static final String EMPTY_MENU_MESSAGE = "메뉴 정보가 존재하지 않습니다.";

    public EmptyMenuException() {
        super(EMPTY_MENU_MESSAGE);
    }
}
