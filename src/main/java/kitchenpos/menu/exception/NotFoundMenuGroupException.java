package kitchenpos.menu.exception;

public class NotFoundMenuGroupException extends RuntimeException {
    public NotFoundMenuGroupException() {
        super("존재 하지 않는 메뉴 그룹 입니다.");
    }
}
