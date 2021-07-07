package kitchenpos.product.exception;

public class ProductPriceEmptyException extends RuntimeException {

    public ProductPriceEmptyException() {
        super("상품의 가격은 필수 입력 항목입니다.");
    }
}
