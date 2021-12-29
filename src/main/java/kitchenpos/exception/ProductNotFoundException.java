package kitchenpos.exception;

public class ProductNotFoundException extends RuntimeException {
    public static final String PRODUCT_GROUP_NOT_FOUND = "제품을 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(PRODUCT_GROUP_NOT_FOUND);
    }
}
