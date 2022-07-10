package kitchenpos.menu.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final String message = "상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(message);
    }
}
