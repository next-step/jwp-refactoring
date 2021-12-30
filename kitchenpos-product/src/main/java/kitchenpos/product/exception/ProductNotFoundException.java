package kitchenpos.product.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_EXIST_PRODUCT = "상품 정보가 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(ERROR_MESSAGE_NOT_EXIST_PRODUCT);
    }
}
