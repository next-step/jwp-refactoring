package kitchenpos.common.exceptions;

public class EmptyProductException extends CustomException {
    public static final String PRODUCT_REQUIRED_EXCEPTION = "상품은 존재해야 합니다.";

    public EmptyProductException() {
        super(PRODUCT_REQUIRED_EXCEPTION);
    }
}
