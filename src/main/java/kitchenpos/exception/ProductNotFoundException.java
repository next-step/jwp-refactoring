package kitchenpos.exception;

public class ProductNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "상품을 찾을 수 없습니다 : %d";

    public ProductNotFoundException(Long productId) {
        super(String.format(DEFAULT_MESSAGE, productId));
    }
}
