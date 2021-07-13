package kitchenpos.menu.exception;

public class NotFoundMenuException extends RuntimeException {
    public NotFoundMenuException() {
        super("찾을 수 없는 메뉴입니다.");
    }
}
