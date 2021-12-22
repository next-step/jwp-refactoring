package kitchenpos.exception;

public class NoMenuException extends RuntimeException {
    public NoMenuException() {
        super("해당하는 메뉴가 없습니다");
    }
}
