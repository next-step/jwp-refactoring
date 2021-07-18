package kitchenpos.common.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException() {
        super("조회된 메뉴가 없습니다.");
    }

    public MenuNotFoundException(String message) {
        super(message);
    }
}
