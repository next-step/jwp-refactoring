package kitchenpos.exception;

import java.math.BigDecimal;
import kitchenpos.domain.Price;

public class MenuPriceException extends RuntimeException {

    private static final String INVALID_MENU_PRICE = "메뉴(Menu)의 가격(Price)는 상품(Product)의 총합보다 작아야 합니다. (price = %s, sum = %s)";

    public MenuPriceException(Price price, BigDecimal sum) {
        super(String.format(INVALID_MENU_PRICE, price.getValue(), sum));
    }
}
