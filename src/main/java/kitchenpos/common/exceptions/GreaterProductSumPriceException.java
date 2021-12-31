package kitchenpos.common.exceptions;

public class GreaterProductSumPriceException extends CustomException {
    public static final String GREATER_PRODUCT_SUM_PRICE_MESSAGE = "메뉴의 가격보다 메뉴 상품의 총 가격이 크면 안됩니다.";

    public GreaterProductSumPriceException() {
        super(GREATER_PRODUCT_SUM_PRICE_MESSAGE);
    }
}
