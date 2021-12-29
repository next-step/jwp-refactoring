package kitchenpos.exception;

public class MenuNotFoundException extends RuntimeException {
    public static final String MENU_NOT_FOUND = "메뉴를 찾을 수 없습니다.";

    public MenuNotFoundException() {
        super(MENU_NOT_FOUND);
    }
}
