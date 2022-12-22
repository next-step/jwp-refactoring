package kitchenpos.menu.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class InvalidMenuPriceException extends BadRequestException {
    public InvalidMenuPriceException() {
        super(INVALID_SUM_OF_PRICE);
    }
}
