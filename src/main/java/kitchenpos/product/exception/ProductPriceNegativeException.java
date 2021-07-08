package kitchenpos.product.exception;

public class ProductPriceNegativeException extends RuntimeException {

    public ProductPriceNegativeException() {
        super("상품의 가격은 음수가 될 수 없습니다.");
    }
}
