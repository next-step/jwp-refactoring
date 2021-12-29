package kitchenpos.menu.exception;

public class MenuNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_EXIST_MENU = "없는 메뉴입니다.";

    public MenuNotFoundException() {
        super(ERROR_MESSAGE_NOT_EXIST_MENU);
    }
}
