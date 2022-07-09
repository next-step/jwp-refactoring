package kitchenpos.menu.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    private static final String message = "메뉴 그룹이 존재하지 않습니다.";

    public MenuGroupNotFoundException() {
        super(message);
    }
}
