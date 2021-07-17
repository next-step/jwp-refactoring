package kitchenpos.menu.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException() {
        super("제품을 찾을 수 없습니다.");
    }
}
