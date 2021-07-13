package kitchenpos.menu.exception;

public class NotFoundProductException extends IllegalArgumentException {

    public NotFoundProductException() {
        super("상품을 찾을 수 없습니다.");
    }
}
