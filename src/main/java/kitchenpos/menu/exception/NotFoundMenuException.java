package kitchenpos.menu.exception;

public class NotFoundMenuException extends RuntimeException {

    public NotFoundMenuException() {
        super("메뉴를 찾을 수 없습니다.");
    }
}
