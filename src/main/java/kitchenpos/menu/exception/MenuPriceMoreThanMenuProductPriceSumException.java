package kitchenpos.menu.exception;

import kitchenpos.global.error.ErrorCode;
import kitchenpos.global.exception.BusinessException;

public class MenuPriceMoreThanMenuProductPriceSumException extends BusinessException {

    public MenuPriceMoreThanMenuProductPriceSumException(final String message) {
        super(message, ErrorCode.MENU_PRICE_MORE_THAN_MENU_PRODUCT);
    }
}
