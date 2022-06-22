package kitchenpos.product.exception;

public class NotFoundProductException extends RuntimeException {

    public NotFoundProductException() {
        super("상품을 찾을 수 없습니다.");
    }
}
