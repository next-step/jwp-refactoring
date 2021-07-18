package kitchenpos.common.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public MenuGroupNotFoundException() {
        super("조회된 메뉴 그룹이 없습니다.");
    }

    public MenuGroupNotFoundException(String message) {
        super(message);
    }
}
