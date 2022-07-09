package kitchenpos.menu.exception;

public class MenuNotFoundException extends RuntimeException {
    private static final String message = "메뉴가 존재하지 않습니다.";

    public MenuNotFoundException() {
        super(message);
    }
}
