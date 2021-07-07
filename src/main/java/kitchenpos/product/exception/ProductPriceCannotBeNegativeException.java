package kitchenpos.product.exception;

public class ProductPriceCannotBeNegativeException extends RuntimeException {

    public ProductPriceCannotBeNegativeException() {
        super("상품의 가격은 음수가 될 수 없습니다.");
    }
}
