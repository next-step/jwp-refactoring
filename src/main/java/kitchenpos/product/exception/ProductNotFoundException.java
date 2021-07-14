package kitchenpos.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("조회된 제품이 없습니다.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
