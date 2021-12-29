package kitchenpos.common.exceptions;

public class MenuProductSumPriceException extends CustomException {
    public static final String MENU_PRODUCT_SUM_PRICE_EXCEPTION = "메뉴의 가격보다 메뉴 상품의 총 가격이 크면 안됩니다.";

    public MenuProductSumPriceException() {
        super(MENU_PRODUCT_SUM_PRICE_EXCEPTION);
    }
}
