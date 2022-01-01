package kitchenpos.common.exception;

import java.math.BigDecimal;

public class MenuProductSumPriceException extends RuntimeException {
    public static final String MENU_PRODUCT_SUM_PRICE_EXCEPTION = "메뉴 가격은 메뉴 상품의 총 합보다 커야합니다. : ";

    public MenuProductSumPriceException(BigDecimal price) {
        super(MENU_PRODUCT_SUM_PRICE_EXCEPTION + price);
    }
}
