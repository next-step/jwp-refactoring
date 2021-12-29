package kitchenpos.common.exceptions;

public class ProductRequiredException extends CustomException {
    public static final String PRODUCT_REQUIRED_EXCEPTION = "상품은 존재해야 합니다.";

    public ProductRequiredException() {
        super(PRODUCT_REQUIRED_EXCEPTION);
    }
}
